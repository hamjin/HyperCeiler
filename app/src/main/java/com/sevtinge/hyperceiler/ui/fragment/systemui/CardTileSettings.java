package com.sevtinge.hyperceiler.ui.fragment.systemui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sevtinge.hyperceiler.R;
import com.sevtinge.hyperceiler.data.adapter.CardTileAdapter;
import com.sevtinge.hyperceiler.data.adapter.CardTileAddAdapter;
import com.sevtinge.hyperceiler.ui.base.BaseSettingsActivity;
import com.sevtinge.hyperceiler.ui.fragment.base.SettingsPreferenceFragment;
import com.sevtinge.hyperceiler.utils.prefs.PrefsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import moralnorm.preference.SwitchPreference;

public class CardTileSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSubSettings().getAppCompatActionBar().setSubtitle("拖拽已添加的开关调整顺序");
    }

    @Override
    public int getContentResId() {
<<<<<<< Updated upstream
        return 0;
    }

    @Override
    public View.OnClickListener addRestartListener() {
        return view -> ((BaseSettingsActivity) getActivity()).showRestartDialog(
                getResources().getString(R.string.system_ui),
                "com.android.systemui"
        );
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_card_tile, container, false);
        return mContentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        mCardTiles = mContentView.findViewById(android.R.id.list);
        mAddCardTiles = mContentView.findViewById(R.id.card_tile_add_list);

        initCardData();
        mCardTileAdapter = new CardTileAdapter(mCardData);
        mAddCardTileAdapter = new CardTileAddAdapter(mAddCardData);

        createCardView(mCardTiles, mCardTileAdapter);
        createCardView(mAddCardTiles, mAddCardTileAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(mCardTiles);

        mCardTileAdapter.setOnDataChangeListener((changed, tile) -> {
            onDataSetChanged(tile, true);
        });

        mAddCardTileAdapter.setOnDataChangeListener((changed, tile) -> {
            onDataSetChanged(tile, false);
        });
    }

    private void createCardView(RecyclerView cardView, RecyclerView.Adapter adapter) {
        mCradLayoutManager = new GridLayoutManager(requireContext(), 2);
        cardView.setLayoutManager(mCradLayoutManager);
        cardView.setAdapter(adapter);
        cardView.setNestedScrollingEnabled(false);
    }

    private List<String> getTileList() {
        String str = PrefsUtils.mSharedPreferences.getString("prefs_key_systemui_plugin_card_tiles", "");
        return TextUtils.isEmpty(str) ? new ArrayList<>() : Arrays.asList(str.split("\\|"));
    }

    private void initCardData() {
        String[] cardTileList = requireContext().getResources().getStringArray(R.array.card_tile_list);
        mCardList = Arrays.asList(cardTileList);
        List<String> tiles = getTileList();
        if (!tiles.isEmpty()) {
            mCardData.clear();
            mCardData.addAll(tiles);
        } else {
            mCardData.addAll(mDefaultCardData);
        }
        if (mAddCardData.isEmpty()) {
            mAddCardData.addAll(mCardList);
        }
        mAddCardData.removeAll(mCardData);
    }

    private void onDataSetChanged(String tile, boolean isAdd) {
        if (isAdd) {
            mCardData.remove(tile);
            mAddCardData.add(tile);
        } else {
            mCardData.add(tile);
            mAddCardData.remove(tile);
        }
        saveList();
        mAddCardTileAdapter.notifyDataSetChanged();
        mCardTileAdapter.notifyDataSetChanged();
    }

    private void saveList() {
        StringBuilder builder = new StringBuilder();
        for (String tile : mCardData) {
            builder.append(tile).append("|");
        }
        String mCardStyleTiles = builder.toString();
        PrefsUtils.putString("prefs_key_systemui_plugin_card_tiles", mCardStyleTiles);
    }

    public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = source.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                //分别把中间所有的item的位置重新交换
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mCardData, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mCardData, i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            saveList();
            //返回true表示执行拖动
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
=======
        return R.xml.system_ui_control_center_card_tile;
>>>>>>> Stashed changes
    }
}
