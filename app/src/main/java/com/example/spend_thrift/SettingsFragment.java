package com.example.spend_thrift;

import static com.example.spend_thrift.Size.*;
import static com.example.spend_thrift.Size.MEDIUM;
import static com.example.spend_thrift.Size.SMALL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.spend_thrift.AppDatabase;
import com.example.spend_thrift.Bucket;
import com.example.spend_thrift.BucketDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsFragment extends Fragment {

    private EditText smallValueEditText;
    private EditText smallTimesEditText;

    private EditText mediumValueEditText;
    private EditText mediumTimesEditText;

    private EditText largeValueEditText;
    private EditText largeTimesEditText;

    private EditText extraLargeValueEditText;
    private EditText extraLargeTimesEditText;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize EditText fields
        smallValueEditText = view.findViewById(R.id.small_value_input);
        smallTimesEditText = view.findViewById(R.id.small_times_input);

        mediumValueEditText = view.findViewById(R.id.medium_value_input);
        mediumTimesEditText = view.findViewById(R.id.medium_times_input);

        largeValueEditText = view.findViewById(R.id.large_value_input);
        largeTimesEditText = view.findViewById(R.id.large_times_input);

        extraLargeValueEditText = view.findViewById(R.id.extra_large_value_input);
        extraLargeTimesEditText = view.findViewById(R.id.extra_large_times_input);

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            EditText smallValueInput = view.findViewById(R.id.small_value_input);
            EditText smallTimesInput = view.findViewById(R.id.small_times_input);
            saveToDatabase("small", smallValueInput.getText().toString(), smallTimesInput.getText().toString());

            EditText mediumValueInput = view.findViewById(R.id.medium_value_input);
            EditText mediumTimesInput = view.findViewById(R.id.medium_times_input);
            saveToDatabase("medium", mediumValueInput.getText().toString(), mediumTimesInput.getText().toString());

            EditText largeValueInput = view.findViewById(R.id.large_value_input);
            EditText largeTimesInput = view.findViewById(R.id.large_times_input);
            saveToDatabase("large", largeValueInput.getText().toString(), largeTimesInput.getText().toString());

            EditText extraLargeValueInput = view.findViewById(R.id.extra_large_value_input);
            EditText extraLargeTimesInput = view.findViewById(R.id.extra_large_times_input);
            saveToDatabase("extra_large", extraLargeValueInput.getText().toString(), extraLargeTimesInput.getText().toString());

            Toast.makeText(getActivity(), "Settings saved", Toast.LENGTH_SHORT).show();
        });

        // Load data from the database
        loadDataFromDatabase();
    }


    private void saveToDatabase(String size, String value, String times) {
        /*AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "buckets-database").build();
        BucketDao bucketDao = db.bucketDao();*/
        int intVal = Integer.parseInt(value);
        int intTimes = Integer.parseInt(times);
        Size enumSize = Size.fromStringValue(size);
        Bucket bucket = new Bucket(enumSize, intVal, intTimes);
        executor.execute(() -> {
            AppDatabase.getInstance(requireContext()).bucketDao().insert(bucket);
        });
    }

    private void loadDataFromDatabase() {
        executor.execute(() -> {
            List<Bucket> buckets = AppDatabase.getInstance(requireContext()).bucketDao().getAll();
            requireActivity().runOnUiThread(() -> {
                for (Bucket bucket : buckets) {
                    updateUIForBucket(bucket);
                }
            });
        });
    }

    private void updateUIForBucket(Bucket bucket) {
        switch (bucket.getSize()) {
            case SMALL:
                smallValueEditText.setText(String.valueOf(bucket.getValue()));
                smallTimesEditText.setText(String.valueOf(bucket.getTokens()));
                break;
            case MEDIUM:
                mediumValueEditText.setText(String.valueOf(bucket.getValue()));
                mediumTimesEditText.setText(String.valueOf(bucket.getTokens()));
                break;
            case LARGE:
                largeValueEditText.setText(String.valueOf(bucket.getValue()));
                largeTimesEditText.setText(String.valueOf(bucket.getTokens()));
                break;
            case EXTRA_LARGE:
                extraLargeValueEditText.setText(String.valueOf(bucket.getValue()));
                extraLargeTimesEditText.setText(String.valueOf(bucket.getTokens()));
                break;
        }
    }

}

enum Size {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    EXTRA_LARGE("extra_large");

    private String stringValue;

    Size(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static Size fromStringValue(String stringValue) {
        for (Size size : Size.values()) {
            if (size.getStringValue().equalsIgnoreCase(stringValue)) {
                return size;
            }
        }
        throw new IllegalArgumentException("Invalid string value for Size: " + stringValue);
    }
}

