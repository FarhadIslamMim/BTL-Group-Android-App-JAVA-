package square.it.image.picker.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import square.it.image.picker.LineDrawListener;
import square.it.image.picker.MediaItem;
import square.it.image.picker.MediaOptions;
import square.it.image.picker.R;
import square.it.image.picker.utils.MediaUtils;
import square.it.image.picker.utils.Utils;
import square.it.image.picker.widget.CanvasView;
/**
 * @author TUNGDX
 */

/**
 * For crop photo. Only crop one item at same time.
 */
public class LineDrawFragment extends BaseFragment implements OnClickListener {
    private static final String EXTRA_MEDIA_SELECTED = "extra_media_selected";
    private static final String EXTRA_MEDIA_OPTIONS = "extra_media_options";

    private LineDrawListener mLineDrawListener;
    private MediaOptions mMediaOptions;
    private MediaItem mMediaItemSelected;
    private CanvasView mCanvasView;
    private View mCancel;
    private View mSave;
    private LinearLayout mUndo;
    private ProgressDialog mDialog;
    private SaveFileLineDrawTask mSaveFileCroppedTask;
    Activity context;

    public static LineDrawFragment newInstance(MediaItem item,
                                               MediaOptions options) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_MEDIA_SELECTED, item);
        bundle.putParcelable(EXTRA_MEDIA_OPTIONS, options);
        LineDrawFragment cropFragment = new LineDrawFragment();
        cropFragment.setArguments(bundle);
        return cropFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLineDrawListener = (LineDrawListener) activity;
        context = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMediaItemSelected = savedInstanceState
                    .getParcelable(EXTRA_MEDIA_SELECTED);
            mMediaOptions = savedInstanceState
                    .getParcelable(EXTRA_MEDIA_OPTIONS);
        } else {
            Bundle bundle = getArguments();
            mMediaItemSelected = bundle.getParcelable(EXTRA_MEDIA_SELECTED);
            mMediaOptions = bundle.getParcelable(EXTRA_MEDIA_OPTIONS);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MEDIA_OPTIONS, mMediaOptions);
        outState.putParcelable(EXTRA_MEDIA_SELECTED, mMediaItemSelected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mediapicker_line_draw,
                container, false);
        init(root);
        context.setTitle("Underline Target Product");
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), mMediaItemSelected.getUriOrigin());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCanvasView.setImageBitmap(bitmap);
        return root;
    }

    private void init(View view) {
        mCanvasView = (CanvasView) view.findViewById(R.id.line_draw);
        mCancel = view.findViewById(R.id.cancel);
        mSave = view.findViewById(R.id.save);
        mUndo = view.findViewById(R.id.undo);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mUndo.setOnClickListener(this);
        mCanvasView.setMode(CanvasView.Mode.DRAW);    // for drawing
        mCanvasView.setDrawer(CanvasView.Drawer.LINE);              // Draw Line
        mCanvasView.setPaintStyle(Paint.Style.FILL_AND_STROKE);
        mCanvasView.setPaintStrokeColor(Color.RED);
        mCanvasView.setPaintStrokeWidth(2F);
        mCanvasView.setLineCap(Paint.Cap.ROUND);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            getFragmentManager().popBackStack();

        } else if (i == R.id.save) {
            mSaveFileCroppedTask = new SaveFileLineDrawTask(context);
            mSaveFileCroppedTask.execute();

        } else if(i == R.id.undo){
            mCanvasView.undo();
        }
    }

    private Uri saveBitmapLineDraw(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        try {
            File file;
            if (mMediaOptions.getCroppedFile() != null) {
                file = mMediaOptions.getCroppedFile();
            } else {
                file = Utils.createTempFile(mContext);
            }
            boolean success = bitmap.compress(CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
            if (success) {
                return Uri.fromFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class SaveFileLineDrawTask extends AsyncTask<Void, Void, Uri> {
        private WeakReference<Activity> reference;

        public SaveFileLineDrawTask(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (reference.get() != null && mDialog == null
                    || !mDialog.isShowing()) {
                mDialog = ProgressDialog.show(reference.get(), null, reference
                        .get().getString(R.string.waiting), false, false);
            }
        }

        @Override
        protected Uri doInBackground(Void... params) {
            Uri uri = null;
            // must try-catch, maybe getCroppedImage() method crash because not
            // set bitmap in mCropImageView
            try {
                Bitmap bitmap = mCanvasView.getBitmap();
                uri = saveBitmapLineDraw(bitmap);
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return uri;
        }

        @Override
        protected void onPostExecute(Uri result) {
            super.onPostExecute(result);
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
            if(mMediaOptions.isCropped()) {
                mMediaItemSelected.setUriCropped(result);
                mLineDrawListener.onLineDrawSuccess(mMediaItemSelected);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSaveFileCroppedTask != null) {
            mSaveFileCroppedTask.cancel(true);
            mSaveFileCroppedTask = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCanvasView = null;
        mDialog = null;
        mSave = null;
        mCancel = null;
    }
}