package com.muhammad.memes.meme_editor.presentation

import androidx.activity.compose.BackHandler
import com.muhammad.memes.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.memes.core.presentation.MemeTemplate
import com.muhammad.memes.meme_editor.presentation.components.BottomBar
import com.muhammad.memes.meme_editor.presentation.components.ConfirmationDialog
import com.muhammad.memes.meme_editor.presentation.components.ConfirmationDialogConfig
import com.muhammad.memes.meme_editor.presentation.components.DraggableContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MemeEditorScreen(
    navHostController: NavHostController,
    memeTemplate: MemeTemplate,
    viewModel: MemeEditorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.hasLeftEditor) {
        if (state.hasLeftEditor) {
            navHostController.navigateUp()
        }
    }
    MemeEditorScreenContent(
        memeTemplate = memeTemplate,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MemeEditorScreenContent(
    memeTemplate: MemeTemplate,
    state: MemeEditorState,
    onAction: (MemeEditorAction) -> Unit,
) {
    var windowSize by remember { mutableStateOf(Size.Zero) }
    BackHandler(enabled = !state.isLeavingWithoutSaving) {
        onAction(MemeEditorAction.OnGoBack)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    onAction(MemeEditorAction.OnTapOutsideSelectedText)
                }
            }, bottomBar = {
            BottomBar(onAddTextClick = {
                onAction(MemeEditorAction.OnAddTextClick)
            }, onSaveClick = {
                onAction(MemeEditorAction.OnSaveMemeClick(memeTemplate))
            })
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Box {
                Image(
                    painter = painterResource(memeTemplate.drawable),
                    contentDescription = null,
                    contentScale = if (windowSize.width > windowSize.height) ContentScale.FillHeight else ContentScale.FillWidth,
                    modifier = Modifier
                        .then(if (windowSize.width > windowSize.height) Modifier.fillMaxHeight() else Modifier.fillMaxWidth())
                        .onSizeChanged { size ->
                            onAction(MemeEditorAction.OnContainerSizeChange(size))
                        }
                )
                DraggableContainer(
                    children = state.memeTexts,
                    onChildTextChange = { id, text ->
                        onAction(MemeEditorAction.OnMemeTextChange(id, text))
                    },
                    onChildClick = { id ->
                        onAction(MemeEditorAction.OnSelectMemeText(id))
                    },
                    onChildDoubleClick = { id ->
                        onAction(MemeEditorAction.OnEditMemeText(id))
                    },
                    onChildDeleteClick = { id ->
                        onAction(MemeEditorAction.OnDeleteMemeText(id))
                    }, modifier = Modifier.matchParentSize(),
                    onChildTransformChange = { id, offset, scale, rotation ->
                        onAction(
                            MemeEditorAction.OnMemeTextTransformChange(
                                id = id,
                                offset = offset,
                                scale = scale,
                                rotation = rotation
                            )
                        )
                    },
                    textBoxInteractionState = state.textBoxInteractionState
                )
            }
            IconButton(onClick = {
                onAction(MemeEditorAction.OnGoBack)
            }, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        }
    }
    if (state.isLeavingWithoutSaving) {
        ConfirmationDialog(
            config = ConfirmationDialogConfig(
                title = stringResource(R.string.leave_editor_title),
                message = stringResource(R.string.leave_editor_message),
                confirmButtonText = stringResource(R.string.leave),
                cancelButtonText = stringResource(R.string.cancel),
                confirmButtonColor = MaterialTheme.colorScheme.secondary
            ),
            onConfirm = {
                onAction(MemeEditorAction.OnConfirmLeaveWithoutSaving)
            },
            onDismiss = {
                onAction(MemeEditorAction.OnDismissLeaveWithoutSaving)
            }
        )
    }
}