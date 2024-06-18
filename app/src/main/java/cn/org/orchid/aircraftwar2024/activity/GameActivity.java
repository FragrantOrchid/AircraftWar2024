package cn.org.orchid.aircraftwar2024.activity;



import static com.google.android.material.internal.ContextUtils.getActivity;

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
import cn.org.orchid.aircraftwar2024.player.PlayerDaoImpl;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    //1:easy;2:medium;3:hard
    private int gameType=0;

    boolean sound = false;
    public static int screenWidth,screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenHW();

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
            sound = getIntent().getBooleanExtra("sound",false);
        }

        /*TODO:根据用户选择的难度加载相应的游戏界面*/
        BaseGame baseGameView = null;
        switch (gameType) {
            case 1 :
                baseGameView = new EasyGame(this);
                break;
            case 2 :
                baseGameView = new MediumGame(this);
                break;
            case 3 :
                baseGameView = new HardGame(this);
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

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                //game结束，传回结果
                case 1 :
                    Log.v("message","getmessage");
                    setContentView(R.layout.activity_record);
                    int score = (int)message.obj;
                    try {
                        showList();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    inputHard(score);
            }
        }
    };
    /*
    暂时硬编码为test
     */
    private void inputHard(int score) {
        PlayerDaoImpl playerDao = new PlayerDaoImpl();
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

    private void showInputAlert(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入id");
        final EditText input = new EditText(this);
        builder.setView(input);

        //积极按钮
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlayerDaoImpl playerDao = new PlayerDaoImpl();
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

    private void showList() throws IOException, ClassNotFoundException {
        PlayerDaoImpl playerDao = new PlayerDaoImpl();
        playerDao.loadAll();
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        List<Map<String, Object>> players = playerDao.getAllPlayers();
        int rank = 0;
        for(Map<String,Object> player : players) {
            rank++;
            map = new HashMap<String, Object>();
            map.put("rank",rank);
            map.put("id",(String)player.get("id"));
            map.put("score",player.get("score"));
            map.put("date",player.get("date"));
            listitem.add(map);
        }
        ListView list = (ListView) findViewById(R.id.PlayerList);
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                listitem,
                R.layout.listitem,
                new String[]{"rank","id","score","time"},
                new int[]{R.id.rank,R.id.id,R.id.score,R.id.date});
        list.setAdapter(listItemAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
                String text = "删除"+clkmap.get("id").toString();

            }
        });



    }
    /*
    void showDeleteAlert(Map<String, Object>) {

    }
    */


    private List<Map<String,Object>> getPlayerData() {
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        return listitem;
    }
}