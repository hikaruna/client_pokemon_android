package ninja.hikaru.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Created by hikaru on 2015/03/23.
 */
public class MyContentProvider extends ContentProvider {
    HttpClient client;

    private JSONArray getApi() {
        HttpGet req = new HttpGet("https://hikaruna-pokemon.herokuapp.com/monsters.json");
        String jsonStr = null;
        try {
            jsonStr = client.execute(req, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    InputStream content = httpResponse.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    return builder.toString();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new JSONArray(jsonStr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreate() {
        client = new DefaultHttpClient();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = new MatrixCursor(projection);
        JSONArray json = getApi();
        for (int i = 0; i < json.length(); i++) {
            try {
                JSONObject item = json.getJSONObject(i);
                Object[] objectes = new Object[projection.length];
                for (int j = 0; j < projection.length; j++) {
                    String column = projection[j];
                    if (column.equals("_id")) {
                        objectes[j] = item.getInt("id");
                    }else {
                        objectes[j] = item.get(column);
                    }
                }
                cursor.addRow(objectes);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.hoge.monsters";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
