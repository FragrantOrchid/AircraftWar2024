package cn.org.orchid.aircraftwar2024.activity;



import static com.google.android.material.internal.ContextUtils.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.org.orchid.aircraftwar2024.R;
import cn.org.orchid.aircraftwar2024.game.BaseGame;
import cn.org.orchid.aircraftwar2024.game.EasyGame;
import cn.org.orchid.aircraftwar2024.game.HardGame;
import cn.org.orchid.aircraftwar2024.game.MediumGame;
import cn.org.orchid.aircraftwar2024.player.Player;
import cn.org.orchid.aircraftwar2024.player.PlayerDaoImpl;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    //1:easy;2:medium;3:hard
    private int gameType=0;

    boolean sound = false;
    public static int screenWidth,screenHeight;

    //Handle传递消息
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            //game结束，传回结果
            if (message.what == 1) {
                Log.v("message", "getmessage");
                //该场比赛数据存入本地
                int score = (int) message.obj;
                Log.v("message","score is"+score);
                savePlayer(score);
                try {
                    showList();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };
    public void savePlayer(int score){
        PlayerDaoImpl playerDao = new PlayerDaoImpl(this,gameType);
        try {
            playerDao.loadAll();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        playerDao.doAdd(new Player(
                "test",
                new Date(),
                score
        ));
        try {
            playerDao.saveAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("GameActivity","onCreate");
        super.onCreate(savedInstanceState);

        getScreenHW();

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
            sound = getIntent().getBooleanExtra("sound",false);
        }

        /*根据用户选择的难度加载相应的游戏界面*/
        BaseGame baseGameView = null;
        switch (gameType) {
            case 1 :
                baseGameView = new EasyGame(this,handler,sound);
                break;
            case 2 :
                baseGameView = new MediumGame(this,handler,sound);
                break;
            case 3 :
                baseGameView = new HardGame(this,handler,sound);
                break;
            default:
                break;
        }
        setContentView(baseGameView);
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getDisplay().getRealMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }


    @SuppressLint("SetTextI18n")
    private void showList() throws IOException, ClassNotFoundException {
        //布局前台化
        setContentView(R.layout.activity_record);
        Log.v("showlist","beginShow");
        //获取textview
        TextView textView = findViewById(R.id.PlayerTitle);

        //TODO 动态更改标题
        switch (gameType) {
            case 1 :
                textView.setText(R.string.easy_mod);
                break;
            case 2 :
                textView.setText(R.string.medium_mod);
                break;
            case 3 :
                textView.setText(R.string.hard_mod);
                break;
            default:
                break;
        }

        //获得Layout里面的ListView
        ListView listView = (ListView) findViewById(R.id.PlayerList);
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
          this,
                getPlayerData(),
                R.layout.listitem,
                new String[]{"rank","id","score","date"},
                new int[]{R.id.rank,R.id.id,R.id.score,R.id.date}
        );
        //添加并显示
        listView.setAdapter(listItemAdapter);
        //添加单击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("onClick","onceclick");
                Map<String, Object> clkmap = (Map<String, Object>) adapterView.getItemAtPosition(i);
                if(clkmap.containsKey("uuid")) {
                    Log.v("onClick","finduuid");
                    UUID uuid = (UUID) clkmap.get("uuid");
                    showDeleteAlert(uuid);

                }
            }
        });


    }


    void showDeleteAlert(UUID uuid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确认删除此条记录");
        //确认按钮
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlayerDaoImpl playerDao = new PlayerDaoImpl(GameActivity.this,gameType);
                try {
                    playerDao.loadAll();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                playerDao.doDelete(uuid);
                try {
                    playerDao.saveAll();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    playerDao.loadAll();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                try {
                    showList();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //TODO 未成功删除
            }
        });
        //取消按钮
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }



    private List<Map<String,Object>> getPlayerData() throws IOException, ClassNotFoundException {
        PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl(this,gameType);
        playerDaoImpl.loadAll();
        //调用order后转换形式
        playerDaoImpl.Order();
        List<Player> players = playerDaoImpl.getAllPlayers();
        //建立转换后的结构
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        //标题头
        map = new HashMap<String, Object>();
        map.put("rank","rank");
        map.put("id","id");
        map.put("score","score");
        map.put("date","date");
        listitem.add(map);
        //表格内容
        int rank = 0;
        for(Player player : players){
            map = new HashMap<String, Object>();
            rank++;
            map.put("rank",rank);
            map.put("id",player.getPlayerId());
            map.put("score",player.getScore());
            map.put("date",player.getDate());
            map.put("uuid",player.getUUID());
            listitem.add(map);
        }
        return listitem;
    }

    public void beginNextGame(View view){
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}