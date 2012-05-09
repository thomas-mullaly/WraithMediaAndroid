package com.wraithmedia.ui.views;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.wraithmedia.R;
import com.wraithmedia.media.MediaSelectionListener;

public class SongsView extends ListFragment {
    public static final String SONGS_DISPLAY_TAG_NAME = "songs";

    private static final String[] MEDIA_COLUMNS_TO_QUERY = {
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM
    };

    private static final int[] MEDIA_COLUMNS_MAPPING = {
            R.id.text_media_details_title,
            R.id.text_media_details_artist,
            R.id.text_media_details_album
    };

    private Cursor mMediaCursor;
    private MediaSelectionListener mMediaSelectionListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle bundle) {
        return inflator.inflate(R.layout.song_view_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // HACK: Use device independent pixels.
        TextView paddingView = new TextView(getActivity());
        paddingView.setHeight(120);
        getListView().addFooterView(paddingView);
        bindToMediaStore();

        setupListView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MediaSelectionListener) {
            mMediaSelectionListener = (MediaSelectionListener)activity;
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        if (mMediaSelectionListener != null) {
            mMediaSelectionListener.onMediaSelected((Cursor)listView.getItemAtPosition(position));
        }
    }
    private void setupListView() {
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    // TODO: Build a content provider to do this for us?
    private void bindToMediaStore() {
        mMediaCursor = getActivity().managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        ListAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.media_details_list_item, mMediaCursor,
                MEDIA_COLUMNS_TO_QUERY, MEDIA_COLUMNS_MAPPING);
        setListAdapter(adapter);
    }
}
