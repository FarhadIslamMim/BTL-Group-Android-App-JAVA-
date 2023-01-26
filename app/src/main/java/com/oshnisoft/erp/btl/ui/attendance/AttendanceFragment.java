package com.oshnisoft.erp.btl.ui.attendance;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.location.LocationService;
import com.oshnisoft.erp.btl.location.LocationUtils;
import com.oshnisoft.erp.btl.model.User;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.AppContainer;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AttendanceFragment extends Fragment {
    private static final String TAG = "AttendanceFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.tieSummary)
    TextInputEditText tieSummary;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtDateTime)
    TextView txtDateTime;
    @BindView(R.id.camera)
    Button camera;
    @BindView(R.id.btnSubmitAttendance)
    Button btnSubmitAttendance;
    @BindView(R.id.faceDetect)
    ImageView faceDetect;

    File file;
    Uri mUri;

    public LoadingDialog loadingDialog;
    private CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;
    @Inject
    User user;
    double latitude = 0.0, longitude = 0.0;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = intent.getDoubleExtra("lat", 0.0);
            longitude = intent.getDoubleExtra("lon", 0.0);
        }
    };

    public AttendanceFragment() {

    }

    public static AttendanceFragment newInstance(String param1) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requireContext().registerReceiver(broadcastReceiver, new IntentFilter("locationReceiver"));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attendance, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    public void init() {
        App.getComponent().inject(this);
        loadingDialog = LoadingDialog.newInstance(getContext(), "Please wait...");
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocation();
        startCamera();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic_" + System.currentTimeMillis() + ".jpg");
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("SAVE", file.getAbsolutePath()).apply();
                mUri = FileProvider.getUriForFile(requireContext(), "com.oshnisoft.erp.btl.provider", file);
                Intent m = new Intent("android.media.action.IMAGE_CAPTURE");
                m.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                m.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    m.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT);
                    m.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    m.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                } else {
                    m.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT); // Tested on API 27 Android version 8.0(Nexus 6P)
                    m.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                    m.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                    m.putExtra("android.intent.extras.CAMERA_FACING", 1);
                }
                someActivityResultLauncher.launch(m);
            }
        });

        btnSubmitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAttendance();
            }
        });
    }

    public void startCamera(){
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic_" + System.currentTimeMillis() + ".jpg");
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("SAVE", file.getAbsolutePath()).apply();
        mUri = FileProvider.getUriForFile(requireContext(), "com.oshnisoft.erp.btl.provider", file);
        Intent m = new Intent("android.media.action.IMAGE_CAPTURE");
        m.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        m.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            m.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT);
            m.putExtra("android.intent.extras.CAMERA_FACING", 1);
            m.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
        } else {
            m.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT); // Tested on API 27 Android version 8.0(Nexus 6P)
            m.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            m.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            m.putExtra("android.intent.extras.CAMERA_FACING", 1);
        }
        someActivityResultLauncher.launch(m);
    }
    Bitmap bit;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case RESULT_OK:
                            try {
                                String filepath;
                                if (file == null)
                                    filepath = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("SAVE", file.getAbsolutePath());
                                else
                                    filepath = file.getAbsolutePath();


                                InputImage image = InputImage.fromFilePath(getContext(), Uri.parse("file://" + filepath));
                                detectFaces(image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case RESULT_CANCELED:
                            Toast.makeText(getContext(), "You cancel this operation", Toast.LENGTH_LONG).show();
                            break;
                    }


                }
            });

    private void detectFaces(InputImage image) {
        // [START set_detector_options]
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .setMinFaceSize(0.1f)
                        .enableTracking()
                        .build();
        // [END set_detector_options]

        // [START get_detector]
        FaceDetector detector = FaceDetection.getClient(options);
        // Or use the default options:
        // FaceDetector detector = FaceDetection.getClient();
        // [END get_detector]

        // [START run_detector]
        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        //Toast.makeText(getContext(), "This Picture have " + faces.size() + " faces", Toast.LENGTH_SHORT).show();
                                        btnSubmitAttendance.setEnabled(false);
                                        if (faces.size() == 0) {
                                            //Toast.makeText(requireContext(), "No Face Detected.", Toast.LENGTH_LONG).show();
                                            faceDetect.setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.bussiness_man));
                                        } else if (faces.size() == 1) {
                                            Rect bounds = faces.get(0).getBoundingBox();
                                            bit = cropBitmap(image.getBitmapInternal(), bounds);
                                            if (bit != null) {
                                                btnSubmitAttendance.setEnabled(true);
                                                faceDetect.setImageBitmap(bit);
                                            }
                                        } else {
                                            //Toast.makeText(requireContext(), "Multiple Face Detected.", Toast.LENGTH_LONG).show();
                                            faceDetect.setImageDrawable(requireActivity().getResources().getDrawable(R.drawable.bussiness_man));
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
        // [END run_detector]
    }

    private void faceOptionsExamples() {
        // [START mlkit_face_options_examples]
        // High-accuracy landmark detection and face classification
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        // Real-time contour detection
        FaceDetectorOptions realTimeOpts =
                new FaceDetectorOptions.Builder()
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .build();
        // [END mlkit_face_options_examples]
    }

    private void processFaceList(List<Face> faces) {
        // [START mlkit_face_list]
        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
            float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
            // nose available):
            FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
            if (leftEar != null) {
                PointF leftEarPos = leftEar.getPosition();
            }

            // If contour detection was enabled:
            List<PointF> leftEyeContour =
                    face.getContour(FaceContour.LEFT_EYE).getPoints();
            List<PointF> upperLipBottomContour =
                    face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();

            // If classification was enabled:
            if (face.getSmilingProbability() != null) {
                float smileProb = face.getSmilingProbability();
            }
            if (face.getRightEyeOpenProbability() != null) {
                float rightEyeOpenProb = face.getRightEyeOpenProbability();
            }

            // If face tracking was enabled:
            if (face.getTrackingId() != null) {
                int id = face.getTrackingId();
            }
        }
    }
    String address;
    public void getLocation(){
        address = "Address: " + LocationUtils.getAddress(requireContext(), LocationService.updateLat, LocationService.updateLon);
        String currentDateTime = "Date and Time: "+ DateTimeUtils.getTodaysDateTime(DateTimeUtils.FORMAT_APP_DEFAULT);
        txtAddress.setText(address);
        txtDateTime.setText(currentDateTime);
    }


    public void submitAttendance() {

        if(AppContainer.KEY_ATTENDANCE_STATUS == 0){
            mParam1 = "In";
        } else {
            mParam1 = "Out";
        }
        SubmitAttendanceModel model = new SubmitAttendanceModel();
        model.setLongitude(LocationService.updateLon);
        model.setLatitude(LocationService.updateLat);
        model.setAttendanceDate(DateTimeUtils.getTodayDate());
        model.setLoginTime(DateTimeUtils.getCurrentTime());
        model.setEmployeeId(user.getEmployee_id());
        model.setImage(getFileDataFromUri());
        model.setType(mParam1);
        model.setAddress(address);
        String note = "";
        if(!TextUtils.isEmpty(tieSummary.getText())){
            note = tieSummary.getText().toString();
        }
        model.setNote(note);
        loadingDialog.show();
        String auth_token = SharedPrefsUtils.getStringPreference(requireContext(), StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable.add(apiServices.setAttendance(auth_token, model)
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<AttendanceModel>() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        Log.e(TAG, "onError login: " + e.toString());
                        ToastUtils.longToast("Submit attendance error: " + e.toString());
                    }

                    @Override
                    public void onNext(AttendanceModel value) {
                        if (value.getStatus()) {
                            ToastUtils.shortToast(value.getMessage());
                            requireActivity().onBackPressed();
                        } else {
                            ToastUtils.shortToast("Login Failed " + value.getMessage());
                        }
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            requireActivity().registerReceiver(broadcastReceiver, new IntentFilter("locationReceiver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            requireActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap cropBitmap(Bitmap bitmap, Rect rect) {
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        Bitmap ret = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(bitmap, -rect.left, -rect.top, null);
        return ret;
    }

    public String getFileDataFromUri() {
        String encoded;
        //BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = calculateInSampleSize(options, 500, 500);
        //options.inJustDecodeBounds = false;
        //Bitmap smallBitmap = BitmapFactory.decodeFile(uri.getPath(), options);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        encoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return encoded;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}