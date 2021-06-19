package net.daverix.urlforward

sealed class SaveFilterAction {
    object CloseSuccessfully : SaveFilterAction()
    object Cancel : SaveFilterAction()
}
