package square.it.image.picker.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import square.it.image.picker.Bucket;
import square.it.image.picker.BucketAdapter;
import square.it.image.picker.LineDrawListener;
import square.it.image.picker.MediaItem;
import square.it.image.picker.MediaOptions;
import square.it.image.picker.R;
import square.it.image.picker.utils.Utils;
import square.it.image.picker.widget.CanvasView;
/**
 * @author TUNGDX
 */

/**
 * For crop photo. Only crop one item at same time.
 */
public class BucketFragment extends BaseFragment implements BucketAdapter.OnItemClickListener {
    private static final String EXTRA_MEDIA_OPTIONS = "extra_media_options";


    private MediaOptions mMediaOptions;

    RecyclerView rvBucket;
    TextView txtNoItem;
    BucketAdapter bucketAdapter;
    Activity context;


    public static BucketFragment newInstance(MediaOptions options) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MEDIA_OPTIONS, options);
        BucketFragment bucketFragment = new BucketFragment();
        bucketFragment.setArguments(bundle);
        return bucketFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMediaOptions = savedInstanceState
                    .getParcelable(EXTRA_MEDIA_OPTIONS);
        } else {
            Bundle bundle = getArguments();
            mMediaOptions = bundle.getParcelable(EXTRA_MEDIA_OPTIONS);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MEDIA_OPTIONS, mMediaOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bucket_picker,
                container, false);

        context.setTitle("Select Folder");
        init(root);

        return root;
    }

    private void init(View view) {
        rvBucket = view.findViewById(R.id.rvBucket);
        txtNoItem = view.findViewById(R.id.no_data);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        bucketAdapter = new BucketAdapter(getImageBuckets(getContext()), this);
        rvBucket.setHasFixedSize(true);
        rvBucket.setLayoutManager(layoutManager);
        rvBucket.setAdapter(bucketAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public static List<String> getImageBuckets(Context mContext){
        HashSet<String> bucketSet = new HashSet<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            File file;
            while (cursor.moveToNext()){
                String bucketPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                String fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(fisrtImage);
                if (file.exists() ) {
                    bucketSet.add(bucketPath);
                }
            }
            cursor.close();
        }

        List<String> buckets = new ArrayList<>(bucketSet);
        return buckets;
    }

    @Override
    public void onItemClick(String item) {
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,
                        MediaPickerFragment.newInstance(mMediaOptions, item))
                .addToBackStack(null)
                .commit();
    }
}