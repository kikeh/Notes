package es.kike.notes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private final int MAX_NOTES = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        final ListView notesList = (ListView) findViewById(R.id.notesList);

        final EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        noteStoreClient.findNotesAsync(new NoteFilter(), 0, MAX_NOTES, new EvernoteCallback<NoteList>() {
            @Override
            public void onSuccess(NoteList result) {
                notesList.setAdapter(getListOfNotes(result));
            }

            @Override
            public void onException(Exception exception) {
                Log.i("Kike", "Exception");
            }
        });
    }

    private ArrayAdapter<String> getListOfNotes(NoteList notes) {
        List<Note> userNotes = notes.getNotes();
        List<String> notesTitles = new ArrayList<String>();

        for(Note note : userNotes) {
            notesTitles.add(note.getTitle());
        }

        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, notesTitles);
    }
}
