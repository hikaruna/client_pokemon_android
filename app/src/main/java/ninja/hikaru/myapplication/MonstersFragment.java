package ninja.hikaru.myapplication;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

public class MonstersFragment extends ListFragment {

    SimpleCursorAdapter adapter;
    String[] columnNames = {"_id", "evolution_from_id", "no"};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        String[] menuCols = new String[]{"_id", "item"};
//        int[] to = new int[]{android.R.id.list, android.R.id.text1};

//        MatrixCursor menuCursor = new MatrixCursor(menuCols);
//        menuCursor.addRow(new Object[]{"1", "Chicken Sandwich"});

        adapter = new SimpleCursorAdapter(
                getActivity(), android.R.layout.simple_list_item_2, null, columnNames ,
                new int[]{0, android.R.id.text1, android.R.id.text2 }, 0);

        setListAdapter(adapter);
//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
//        stringArrayAdapter.add("hello");
//        setListAdapter(stringArrayAdapter);
        setListShown(false);

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(), Uri.parse("content://hoge/monsters"), columnNames, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
                adapter.notifyDataSetChanged();

                if (isResumed()) {
                    setListShown(true);
                } else {
                    setListShownNoAnimation(true);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

}
