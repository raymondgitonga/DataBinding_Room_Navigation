package com.tosh.roomnav.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.tosh.roomnav.R
import com.tosh.roomnav.room.Note
import com.tosh.roomnav.room.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            titleEt.setText(note?.title)
            bodyEt.setText(note?.body)
        }

        buttonSave.setOnClickListener {
            val noteTitle = titleEt.text.toString().trim()
            val noteBody = bodyEt.text.toString().trim()

            if (noteTitle.isNullOrEmpty()) {
                titleEt.error = "Title required"
                return@setOnClickListener
            }
            if (noteBody.isNullOrEmpty()) {
                bodyEt.error = "Note body required"
                return@setOnClickListener
            }

            val newNote = Note(noteTitle,noteBody)

            launch {
               if (note == null){
                   saveNote(newNote)
               }else{
                   updateNote(newNote)
               }

            }
            navigateBack()
        }
    }

    private suspend fun updateNote(newNote: Note){
        newNote.id = note!!.id
        context?.let {
            NoteDatabase(it).getNoteDao().updateNote(newNote)
            Toast.makeText(it, "Note Updated", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun saveNote(note: Note) {
        context?.let {
            NoteDatabase(it).getNoteDao().addNote(note)
            Toast.makeText(it, "Note Created", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.deleteNote -> {
                note?.let {
                    deleteNote()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote(){
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Are you sure?")
                setMessage("Cannot be undone")
                setPositiveButton("OK"){dialog, which ->
                    launch {
                        NoteDatabase(it).getNoteDao().deleteNote(note!!)
                        navigateBack()
                    }
                }
                setNegativeButton("Cancel"){ _, _ ->  }
                show()
            }
        }
    }

    private fun navigateBack() {
        val action = AddNoteFragmentDirections.actionSaveNote()
        Navigation.findNavController(buttonSave).navigate(action)
    }


}
