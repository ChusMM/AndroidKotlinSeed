package com.example.androidkotlinseed.view.dialogs

import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.UiThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

@UiThread
class DialogsManager(private val fragmentManager: FragmentManager) {

    companion object {
        const val EMPTY_DIALOG_ID = ""

        /**
         * Whenever a dialog is shown with non-empty "id", the provided id will be stored in
         * arguments Bundle under this key.
         */
        const val ARGUMENT_DIALOG_ID = "ARGUMENT_DIALOG_ID"

        /**
         * In case Activity or Fragment that instantiated this DialogsManager are re-created (e.g.
         * in case of memory reclaim by OS, orientation change, etc.), we need to be able
         * to get a reference to dialog that might have been shown. This tag will be supplied with
         * all DialogFragment's shown by this DialogsManager and can be used to query
         * {@link FragmentManager} for last shown dialog.
         */
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"
    }

    private var currentlyShownDialog: DialogFragment? = null

    init {
        val fragmentDialogWithTag: Fragment? = fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        if (fragmentDialogWithTag != null && DialogFragment::class.java.isAssignableFrom(fragmentDialogWithTag::class.java)) {
            currentlyShownDialog = fragmentDialogWithTag as DialogFragment
        }
    }

    /**
     * @return a reference to currently shown dialog, or null if no dialog is shown.
     */
    fun getCurrentlyShownDialog(): DialogFragment? = currentlyShownDialog

    /**
     * Obtain the id of the currently shown dialog.
     * @return the id of the currently shown dialog; empty string if no dialog is shown, or the currently
     *         shown dialog has no id
     */
    private fun getCurrentlyShownDialogId(): String {
        return if (currentlyShownDialog == null ||
            currentlyShownDialog?.arguments == null ||
            currentlyShownDialog?.arguments?.containsKey(ARGUMENT_DIALOG_ID) == false) {
            EMPTY_DIALOG_ID
        } else {
            currentlyShownDialog?.arguments?.getString(ARGUMENT_DIALOG_ID) ?: EMPTY_DIALOG_ID
        }
    }

    /**
     * Check whether a dialog with a specified id is currently shown
     * @param id dialog id to query
     * @return true if a dialog with the given id is currently shown; false otherwise
     */

    fun isDialogShowingWithId(id: String): Boolean {
        val shownDialogId = getCurrentlyShownDialogId()
        return !TextUtils.isEmpty(shownDialogId) && shownDialogId == id
    }

    /**
     * Dismiss the currently shown dialog. Has no effect if no dialog is shown. Please note that
     * we always allow state loss upon dismissal.
     */
    fun dismissCurrentlyShownDialog() {
        currentlyShownDialog?.let {
            it.dismissAllowingStateLoss()
            currentlyShownDialog = null
        }
    }

    /**
     * Show dialog and assign it a given "id". Replaces any other currently shown dialog.
     * @param dialog dialog to show
     * @param id string that uniquely identifies the dialog; can be null
     */
    fun showDialogWithId(dialog: DialogFragment, id: String) {
        dismissCurrentlyShownDialog()
        setId(dialog, id)
        showDialog(dialog)
    }

    private fun setId(dialogFragment: DialogFragment, id: String) {
        val args = dialogFragment.arguments ?: Bundle(1)
        args.putString(ARGUMENT_DIALOG_ID, id)
        dialogFragment.arguments = args
    }

    private fun showDialog(dialogFragment: DialogFragment) {
        fragmentManager.beginTransaction()
            .add(dialogFragment, DIALOG_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }
}