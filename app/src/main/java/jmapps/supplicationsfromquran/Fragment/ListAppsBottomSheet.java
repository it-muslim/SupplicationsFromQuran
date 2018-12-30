package jmapps.supplicationsfromquran.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.Arrays;
import java.util.Objects;

import jmapps.supplicationsfromquran.Adapter.ListAppsAdapter;
import jmapps.supplicationsfromquran.R;

import static jmapps.supplicationsfromquran.Model.ListAppModel.listAppModel;

public class ListAppsBottomSheet extends BottomSheetDialogFragment {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listAppsRoot = inflater.inflate(R.layout.bottom_sheet_list_apps, container, false);

        setRetainInstance(true);
        Objects.requireNonNull(getDialog().getWindow()).requestFeature(Window.FEATURE_NO_TITLE);

        RecyclerView rvListApps = listAppsRoot.findViewById(R.id.rv_list_apps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvListApps.setLayoutManager(linearLayoutManager);
        rvListApps.setHasFixedSize(true);

        ListAppsAdapter listAppsAdapter = new ListAppsAdapter(Arrays.asList(listAppModel));
        rvListApps.setAdapter(listAppsAdapter);

        return listAppsRoot;
    }
}