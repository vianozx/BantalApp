package com.example.bantalapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ViewJournalActivity : AppCompatActivity(), StartGameDialogFragment.OnDeleteListener {
    private lateinit var tvJudul :TextView
    private lateinit var tvIsi :TextView
    private  lateinit var isi:String
    private  var judul:String ="Loading......"
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var btnSave: FloatingActionButton
    private lateinit var journal: JournalControl
    private lateinit var backBtn: ImageButton
    private lateinit var btnDel: Button
    private lateinit var btnEdit: Button
    override fun onJournalDeleted() {
        // Notify user of successful deletion
        Toast.makeText(this, "Journal deleted successfully", Toast.LENGTH_SHORT).show()

        // Finish the activity and navigate back
        onBackPressed()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_journal)
        tvJudul = findViewById(R.id.tvJudulView)
        tvIsi = findViewById(R.id.tvContentView)
        btnSave = findViewById(R.id.btnSave)
        backBtn = findViewById(R.id.backButton)
        btnDel = findViewById(R.id.btnDel)
        btnEdit = findViewById(R.id.btnEdit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val journalTitle = intent.getStringExtra("journalTitle")
        val documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            firestore.collection("Journal").document(documentId).get().addOnSuccessListener { document ->
                    judul = document.getString("NoteTitle")?:"error"
                    isi = document.getString("NoteContent")?:"error"
                    tvIsi.text = isi
                    tvJudul.text=judul



            }
        }
        // Use the documentId to fetch more details or do further processing
        Log.d("ViewJournalActivity", "Received document ID: $documentId")
        btnSave.setOnClickListener(View.OnClickListener {
            val journal = hashMapOf(
                "NoteTitle" to tvJudul.text.toString(),
                "NoteContent" to tvIsi.text.toString()
            )
            if (documentId != null) {
                firestore.collection("Journal").document(documentId).update(journal as Map<String, Any>).addOnCompleteListener(this){ task ->
                    if (task.isComplete){
                        Toast.makeText(
                            applicationContext,
                            "Berhasil disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                    else{
                        Toast.makeText(
                            applicationContext,
                            "mohon maaf tidak dapat diedit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
        backBtn.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        btnDel.setOnClickListener(View.OnClickListener {
            val args = Bundle()
            args.putString("key", documentId)
            val dialogFragment = StartGameDialogFragment()
            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "DELETE_DIALOG")

        })
        btnEdit.setOnClickListener(View.OnClickListener {
            enableEditText(tvIsi)
            enableEditText(tvJudul)
            btnSave.isVisible=true
        })
    }

//    @SuppressLint("NewApi")
//    fun showPopup(v : View){
//        val popup = PopupMenu(this, v)
//        val inflater: MenuInflater = popup.menuInflater
//        inflater.inflate(R.menu.notemenu, popup.menu)
//        popup.setOnMenuItemClickListener { menuItem ->
//            when(menuItem.itemId){
//                R.id.edit-> {
//                            enableEditText(tvIsi)
//                            enableEditText(tvJudul)
//                            btnSave.isVisible=true
//                }
//                R.id.delete-> {deleteJournal()
//
//                }
//            }
//            true
//        }
//        popup.setOnDismissListener {
//            // Respond to popup being dismissed.
//        }
//        popup.setForceShowIcon(true)
//        popup.show()
//    }
    @SuppressLint("NewApi")
    fun enableEditText(tv:TextView){
        tv.isFocusable=true
        tv.isFocusableInTouchMode =true
        tv.isEnabled=true
        tv.isCursorVisible=true
    }



}
class StartGameDialogFragment : DialogFragment() {
    private lateinit var firestore: FirebaseFirestore
    interface OnDeleteListener {
        fun onJournalDeleted()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDeleteListener) {
            // Listener is properly attached
        } else {
            throw ClassCastException("$context must implement OnDeleteListener")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val documentId = arguments?.getString("key")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Apakah anda yakin ingin menghapus jurnal ini?")
                .setPositiveButton("Delete") { dialog, id ->
                    documentId?.let { id ->
                        firestore.collection("Journal").document(id).delete()
                            .addOnSuccessListener {
                                (activity as? OnDeleteListener)?.onJournalDeleted()
                                dismiss()
                            }
                            .addOnFailureListener { exception ->

                            }
                    }
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
