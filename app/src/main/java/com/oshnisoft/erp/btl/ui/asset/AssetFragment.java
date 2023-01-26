package com.oshnisoft.erp.btl.ui.asset;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.model.IAsset;
import com.oshnisoft.erp.btl.model.ResponseDataList;
import com.oshnisoft.erp.btl.net.APIServices;
import com.oshnisoft.erp.btl.utils.LoadingDialog;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;
import com.oshnisoft.erp.btl.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class AssetFragment extends Fragment {

    @BindView(R.id.addAsset)
    FloatingActionButton addAsset;
    @BindView(R.id.rvAsset)
    RecyclerView rvAsset;

    CompositeDisposable mCompositeDisposable;
    @Inject
    APIServices apiServices;
    LoadingDialog loadingDialog;
    FastItemAdapter<IAsset> iAssetFastItemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_asset, container, false);
        App.getComponent().inject(this);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAssets();
        addAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateAssetFragment();
                Bundle b = new Bundle();
                fragment.setArguments(b);
                ((AppCompatActivity) requireContext()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("add_Asset").commit();
            }
        });
    }

    public void getAssets(){
        loadingDialog = LoadingDialog.newInstance(requireContext(), "Please wait...");
        loadingDialog.show();
        String token = SharedPrefsUtils.getStringPreference(requireContext(), StringConstants.PREF_AUTH_TOKEN);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(apiServices.getAsset(token) //test jsonblob
                .subscribeOn(Schedulers.io())  // Run on a background thread
                .observeOn(AndroidSchedulers.mainThread()) // Be notified on the main thread
                .subscribeWith(new DisposableObserver<ResponseDataList<IAsset>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("Server Error: " + e.getMessage());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(ResponseDataList<IAsset> value) {
                        loadingDialog.dismiss();
                        if (value.isSuccess() && value.getDataList() != null && value.getDataList().size() > 0) {
                            generateData(value.getDataList());
                        } else {
                            ToastUtils.longToast("No Data available ");
                        }
                    }
                }));
    }
    void generateData(List<IAsset> value) {
        iAssetFastItemAdapter = new FastItemAdapter<>();
        iAssetFastItemAdapter.add(value);
        iAssetFastItemAdapter.setHasStableIds(true);
        iAssetFastItemAdapter.withSelectable(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        rvAsset.setLayoutManager(layoutManager);
        rvAsset.setAdapter(iAssetFastItemAdapter);

    }


}