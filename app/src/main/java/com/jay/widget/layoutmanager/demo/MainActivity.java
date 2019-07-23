package com.jay.widget.layoutmanager.demo;

import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jay.widget.StickyHeaders;
import com.jay.widget.StickyHeadersGridLayoutManager;
import com.jay.widget.StickyHeadersLinearLayoutManager;
import com.jay.widget.StickyHeadersStaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        setLinearLayoutManager();
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                setLinearLayoutManager();
                break;
            case R.id.menu2:
                setGridLayoutManager();
                break;
            case R.id.menu3:
                setStaggeredGridLayoutManager();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setLinearLayoutManager() {
        StickyHeadersLinearLayoutManager<MyAdapter> layoutManager = new StickyHeadersLinearLayoutManager<>(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setGridLayoutManager() {
        StickyHeadersGridLayoutManager<MyAdapter> layoutManager = new StickyHeadersGridLayoutManager<>(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isStickyHeader(position)) {
                    return 3;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setStaggeredGridLayoutManager() {
        StickyHeadersStaggeredGridLayoutManager<MyAdapter> layoutManager =
                new StickyHeadersStaggeredGridLayoutManager<>(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements StickyHeaders, StickyHeaders.ViewSetup {

        private static final String[] DICT = {
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        };
        private static final int HEADER_ITEM = 123;

        public MyAdapter() {
            initData();
        }

        List<String> datas = new ArrayList<>();

        private void initData() {
            for (int i = 65; i < 26 + 65; i++) {
                datas.add(String.valueOf((char) i));
                for (int j = 0; j < 10; j++) {
                    String itemText = getItemText((char) i);
                    datas.add(itemText);
                }
            }
        }

        private String getItemText(char prefix) {
            int length = createRandom(0, 10);
            StringBuilder builder = new StringBuilder();
            builder.append(prefix);
            for (int i = 0; i < length; i++) {
                int random = createRandom(0, 51);
                builder.append(DICT[random]);
            }
            return builder.toString();
        }

        private int createRandom(int min, int max) {
            Random random = new Random();
            return random.nextInt(max) % (max - min + 1) + min;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER_ITEM) {
                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
                return new MyViewHolder(inflate);
            } else {
                View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new MyViewHolder(inflate);
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String item = datas.get(position);
            TextView textView = holder.itemView.findViewById(android.R.id.text1);
            textView.setText(item);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position % 11 == 0 ? HEADER_ITEM : super.getItemViewType(position);
        }

        @Override
        public boolean isStickyHeader(int position) {
            return getItemViewType(position) == HEADER_ITEM;
        }

        @Override
        public void setupStickyHeaderView(View stickyHeader) {
            ViewCompat.setElevation(stickyHeader, 10);
        }

        @Override
        public void teardownStickyHeaderView(View stickyHeader) {
            ViewCompat.setElevation(stickyHeader, 0);
        }

        @Override
        public void onViewAttachedToWindow(MyViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                if (isStickyHeader(holder.getLayoutPosition())) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    p.setFullSpan(true);
                }
            }
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
