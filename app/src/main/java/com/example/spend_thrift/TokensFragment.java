package com.example.spend_thrift;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.Executors;

public class TokensFragment extends Fragment {

    private TextView smallValueTextView;
    private TextView mediumValueTextView;
    private TextView largeValueTextView;
    private TextView extraLargeValueTextView;

    public TokensFragment() {
        // Required empty public constructor
    }

    public static TokensFragment newInstance() {
        return new TokensFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tokens, container, false);
    }

    private void updateBucketValues() {
        BucketDao bucketDao = AppDatabase.getInstance(requireContext()).bucketDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Bucket> buckets = bucketDao.getAll();
            for (Bucket bucket : buckets) {
                int bucketValue = bucket.getValue();
                Size size = bucket.getSize();
                if (size == null) continue;

                requireActivity().runOnUiThread(() -> {
                    switch (size) {
                        case SMALL:
                            smallValueTextView.setText(String.valueOf(bucketValue));
                            break;
                        case MEDIUM:
                            mediumValueTextView.setText(String.valueOf(bucketValue));
                            break;
                        case LARGE:
                            largeValueTextView.setText(String.valueOf(bucketValue));
                            break;
                        case EXTRA_LARGE:
                            extraLargeValueTextView.setText(String.valueOf(bucketValue));
                            break;
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        smallValueTextView = view.findViewById(R.id.smallValueTextView);
        mediumValueTextView = view.findViewById(R.id.mediumValueTextView);
        largeValueTextView = view.findViewById(R.id.largeValueTextView);
        extraLargeValueTextView = view.findViewById(R.id.extraLargeValueTextView);

        updateBucketValues();
    }

}
