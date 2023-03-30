package com.panda_cookie.spend_thrift.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.panda_cookie.spend_thrift.data.AppDatabase;
import com.panda_cookie.spend_thrift.AppExecutors;
import com.panda_cookie.spend_thrift.data.Bucket;
import com.panda_cookie.spend_thrift.data.BucketDao;
import com.panda_cookie.spend_thrift.R;
import com.panda_cookie.spend_thrift.Size;
import com.panda_cookie.spend_thrift.data.State;
import com.panda_cookie.spend_thrift.data.StateDao;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class TokensFragment extends Fragment {

    private TextView smallValueTextView;
    private TextView mediumValueTextView;
    private TextView largeValueTextView;
    private TextView extraLargeValueTextView;

    private EditText smallTokensUsedEditText;
    private EditText mediumTokensUsedEditText;
    private EditText largeTokensUsedEditText;
    private EditText extraLargeTokensUsedEditText;

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
        StateDao stateDao = AppDatabase.getInstance(requireContext()).stateDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Bucket> buckets = bucketDao.getAll();
            for (Bucket bucket : buckets) {
                int bucketValue = bucket.getValue();
                Size size = bucket.getSize();

                State state = stateDao.findByBucket(size.name().toLowerCase());

                int tokensUsed = state != null ? state.getTokensUsed() : 0;

                requireActivity().runOnUiThread(() -> {
                    switch (size) {
                        case SMALL:
                            smallValueTextView.setText(String.valueOf(bucketValue));
                            smallTokensUsedEditText.setText(String.valueOf(tokensUsed));
                            break;
                        case MEDIUM:
                            mediumValueTextView.setText(String.valueOf(bucketValue));
                            mediumTokensUsedEditText.setText(String.valueOf(tokensUsed));
                            break;
                        case LARGE:
                            largeValueTextView.setText(String.valueOf(bucketValue));
                            largeTokensUsedEditText.setText(String.valueOf(tokensUsed));
                            break;
                        case EXTRA_LARGE:
                            extraLargeValueTextView.setText(String.valueOf(bucketValue));
                            extraLargeTokensUsedEditText.setText(String.valueOf(tokensUsed));
                            break;
                    }
                });
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Check if settings are not set
        AppExecutors.getInstance().diskIO().execute(() -> {
            AppDatabase db = AppExecutors.getInstance().getDatabase(requireContext());
            boolean isBucketTableEmpty = db.bucketDao().getAllBuckets().isEmpty();

            // Show Snackbar if settings are not set
            if (isBucketTableEmpty) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar snackbar = Snackbar.make(view, "Please set your settings!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("DISMISS", v -> {
                                // Dismiss the Snackbar
                            });
                    snackbar.show();
                });
            }
        });
        smallValueTextView = view.findViewById(R.id.smallValueTextView);
        mediumValueTextView = view.findViewById(R.id.mediumValueTextView);
        largeValueTextView = view.findViewById(R.id.largeValueTextView);
        extraLargeValueTextView = view.findViewById(R.id.extraLargeValueTextView);

        updateBucketValues();

        Button saveButton = view.findViewById(R.id.tokensUsedSaveButton);
        smallTokensUsedEditText = view.findViewById(R.id.smallTimesEditText);
        mediumTokensUsedEditText = view.findViewById(R.id.mediumTimesEditText);
        largeTokensUsedEditText = view.findViewById(R.id.largeTimesEditText);
        extraLargeTokensUsedEditText = view.findViewById(R.id.extraLargeTimesEditText);

        saveButton.setOnClickListener(v -> {
            int tokensUsed = Integer.parseInt(smallTokensUsedEditText.getText().toString());

            // Calculate the start and end dates for the current week.
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            long startDate = calendar.getTimeInMillis();

            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            long endDate = calendar.getTimeInMillis();

            saveTokenData("small", tokensUsed, startDate, endDate);

            int mediumTokensUsed = Integer.parseInt(mediumTokensUsedEditText.getText().toString());

            // Calculate the start and end dates for the current month.
            Calendar monthCalendar = Calendar.getInstance();
            monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
            long monthStartDate = monthCalendar.getTimeInMillis();

            monthCalendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            long monthEndDate = calendar.getTimeInMillis();

            saveTokenData("medium", mediumTokensUsed, monthStartDate, monthEndDate);

            int largeTokensUsed = Integer.parseInt(largeTokensUsedEditText.getText().toString());

            // Calculate the start and end dates for the current quarter.
            Calendar quarterCalendar = Calendar.getInstance();
            int currentMonth = quarterCalendar.get(Calendar.MONTH);
            int firstMonthOfQuarter = currentMonth / 3 * 3;

            quarterCalendar.set(Calendar.MONTH, firstMonthOfQuarter);
            quarterCalendar.set(Calendar.DAY_OF_MONTH, 1);
            long quarterStartDate = quarterCalendar.getTimeInMillis();

            quarterCalendar.set(Calendar.MONTH, firstMonthOfQuarter + 2);
            quarterCalendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            long quarterEndDate = calendar.getTimeInMillis();

            saveTokenData("large", largeTokensUsed, quarterStartDate, quarterEndDate);

            int xlTokensUsed = Integer.parseInt(extraLargeTokensUsedEditText.getText().toString());

            // Calculate the start and end dates for the current year.
            Calendar yearCalendar = Calendar.getInstance();
            yearCalendar.set(Calendar.MONTH, Calendar.JANUARY);
            yearCalendar.set(Calendar.DAY_OF_MONTH, 1);
            long yearStartDate = yearCalendar.getTimeInMillis();

            yearCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
            yearCalendar.set(Calendar.DAY_OF_MONTH, 31);
            long yearEndDate = calendar.getTimeInMillis();

            saveTokenData("extra_large", xlTokensUsed, yearStartDate, yearEndDate);
        });

    }

    private void saveTokenData(String bucket, int tokensUsed, long startDate, long endDate) {
        StateDao stateDao = AppDatabase.getInstance(requireContext()).stateDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            State existingState = stateDao.findByBucket(bucket);

            if (existingState == null) {
                int tokensFromBucket = getTokensForBucket(bucket);
                int tokensLeft = tokensFromBucket - tokensUsed;

                State newState = new State(bucket, startDate, endDate, tokensUsed, tokensLeft);
                stateDao.insert(newState);
            } else {
                existingState.setTokensUsed(tokensUsed);
                existingState.setStartDate(startDate);
                existingState.setEndDate(endDate);
                int tokensFromBucket = getTokensForBucket(bucket);
                existingState.setTokensLeft(tokensFromBucket - tokensUsed);

                stateDao.update(existingState);
            }
        });
    }

    private int getTokensForBucket(String bucket) {
        BucketDao bucketDao = AppDatabase.getInstance(requireContext()).bucketDao();
        Bucket bucketRecord = bucketDao.findByBucket(bucket.toUpperCase());

        if (bucketRecord != null) {
            return bucketRecord.getTokens();
        } else {
            return 0;
        }
    }



}
