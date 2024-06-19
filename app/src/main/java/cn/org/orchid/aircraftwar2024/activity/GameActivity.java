package cn.org.orchid.aircraftwar2024.activity;



import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;
import android.content.DialogInterface;
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


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


                setContentView(R.layout.activity_record);
                try {
                    showList();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //TODO 应当删除这个message以避免重复触发
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
                baseGameView = new EasyGame(this,handler);
                break;
            case 2 :
                baseGameView = new MediumGame(this,handler);
                break;
            case 3 :
                baseGameView = new HardGame(this,handler);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /*
    暂时硬编码为test
     */
    /*
    private void inputHard(int score) {
        PlayerDaoImpl playerDao = new PlayerDaoImpl(gameType);
        try {
            playerDao.loadAll();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> player = null;
        player.put("id","test");
        player.put("score",score);
        player.put("date",new Date());
        playerDao.doAdd(player);
        try {
            playerDao.saveAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            showList();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

     */
    /*
    private void showInputAlert(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入id");
        final EditText input = new EditText(this);
        builder.setView(input);

        //积极按钮
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlayerDaoImpl playerDao = new PlayerDaoImpl(gameType);
                try {
                    playerDao.loadAll();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Map<String, Object> player = null;
                player.put("id",input.getText().toString());
                player.put("score",score);
                player.put("date",new Date());
                playerDao.doAdd(player);
                try {
                    playerDao.saveAll();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    showList();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

     */

    private void showList() throws IOException, ClassNotFoundException {
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
        listView.setAdapter(listItemAdapter);
    }
    /*
    void showDeleteAlert(Map<String, Object>) {

    }
    */


    private List<Map<String,Object>> getPlayerData() throws IOException, ClassNotFoundException {
        PlayerDaoImpl playerDaoImpl = new PlayerDaoImpl(this,gameType);
        playerDaoImpl.loadAll();
        //调用order后转换形式
        playerDaoImpl.Order();
        List<Player> players = playerDaoImpl.getAllPlayers();
        //建立转换后的结构
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
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
}