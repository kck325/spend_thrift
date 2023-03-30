package com.panda_cookie.spend_thrift.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.panda_cookie.spend_thrift.R;
import com.panda_cookie.spend_thrift.adapters.GoalDataAdapter;
import com.panda_cookie.spend_thrift.data.AppDatabase;
import com.panda_cookie.spend_thrift.data.StateDao;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatsFragment extends Fragment {

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.stats_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate header and add to RecyclerView
        View header = LayoutInflater.from(getContext()).inflate(R.layout.header_item, recyclerView, false);
        recyclerView.addItemDecoration(new HeaderItemDecoration(header));

        AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "app_database").build();
        StateDao stateDao = db.stateDao();

        FetchGoalDataTask fetchGoalDataTask = new FetchGoalDataTask(this, stateDao);
        fetchGoalDataTask.execute();
    }

    private void setupRecyclerView(View view, List<GoalDataAdapter.GoalData> goalDataList) {
        RecyclerView recyclerView = view.findViewById(R.id.stats_recycler_view);
        GoalDataAdapter adapter = new GoalDataAdapter(requireContext(), goalDataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private static class FetchGoalDataTask extends AsyncTask<Void, Void, List<GoalDataAdapter.GoalData>> {
        private final WeakReference<StatsFragment> fragmentWeakReference;
        private final StateDao stateDao;

        public FetchGoalDataTask(StatsFragment fragment, StateDao stateDao) {
            this.fragmentWeakReference = new WeakReference<>(fragment);
            this.stateDao = stateDao;
        }

        @Override
        protected List<GoalDataAdapter.GoalData> doInBackground(Void... voids) {
            List<StateDao.GoalsMet> goalsMetList = stateDao.getGoalsMet();
            List<StateDao.GoalsMissed> goalsMissedList = stateDao.getGoalsMissed();
            StatsFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {
                List<GoalDataAdapter.GoalData> goalDataList = fragment.combineGoalData(goalsMetList, goalsMissedList);

                Collections.sort(goalDataList, new Comparator<GoalDataAdapter.GoalData>() {
                    @Override
                    public int compare(GoalDataAdapter.GoalData o1, GoalDataAdapter.GoalData o2) {
                        return o2.getBucket_type().compareTo(o1.getBucket_type());
                    }
                });

                return goalDataList;
            } else {
                return new ArrayList<>();
            }
        }


        @Override
        protected void onPostExecute(List<GoalDataAdapter.GoalData> goalDataList) {
            StatsFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {
                fragment.setupRecyclerView(fragment.getView(), goalDataList);
            }
        }
    }

    private List<GoalDataAdapter.GoalData> combineGoalData(List<StateDao.GoalsMet> goalsMetList, List<StateDao.GoalsMissed> goalsMissedList) {
        List<GoalDataAdapter.GoalData> goalDataList = new ArrayList<>();

        // Create a map to store bucket type as key and goals met count as value
        Map<String, Integer> goalsMetMap = new HashMap<>();
        for (StateDao.GoalsMet goalsMet : goalsMetList) {
            goalsMetMap.put(goalsMet.getBucketType(), goalsMet.getGoalsMet());
        }

        // Create a map to store bucket type as key and goals missed count as value
        Map<String, Integer> goalsMissedMap = new HashMap<>();
        for (StateDao.GoalsMissed goalsMissed : goalsMissedList) {
            goalsMissedMap.put(goalsMissed.getBucketType(), goalsMissed.getGoalsMissed());
        }

        // Combine goals met and goals missed counts for each bucket type
        Set<String> bucketTypes = new HashSet<>(goalsMetMap.keySet());
        bucketTypes.addAll(goalsMissedMap.keySet());
        for (String bucketType : bucketTypes) {
            int goalsMetCount = goalsMetMap.getOrDefault(bucketType, 0);
            int goalsMissedCount = goalsMissedMap.getOrDefault(bucketType, 0);
            goalDataList.add(new GoalDataAdapter.GoalData(bucketType, goalsMetCount, goalsMissedCount));
        }

        return goalDataList;
    }



}

