package cn.org.orchid.aircraftwar2024.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.org.orchid.aircraftwar2024.R;
import cn.org.orchid.aircraftwar2024.game.EasyGame;

public class OnlineActivity extends AppCompatActivity {
    boolean sound;
    EasyGame easyGame;

    int lastScore;
    int enemyLastScore;

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            //game结束，传回结果
            if (message.what == 1) {
                Log.v("message", "getmessage");
                //该场比赛数据存入本地
                lastScore = (int) message.obj;
                Log.v("message","score is"+lastScore);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        sound = getIntent().getBooleanExtra("sound",false);
        //在线游戏在这里直接启动
        easyGame = new EasyGame(this,handler,sound);
        setContentView(easyGame);
        easyGame.pauseGame();
    }





    //内部类完成通讯
    protected class ScoketThread extends Thread{
        private Handler handler;
        private ScheduledExecutorService executorService;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter writer;
        //1:还未发包，2：发过包未匹配，3：已经匹配；
        private int stage;
        public ScoketThread(Handler handler) {
            this.handler = handler;
            this.executorService = Executors.newScheduledThreadPool(1);
            stage = 1;
        }
        @Override
        public void run(){
            Runnable tack = () -> {
                //数据相关变量声明
                int code;
                UUID uuid;
                int score;
                boolean gameover;
                String jsonString;
                JSONObject jsonObject;

                //socket客户端实例化并连接
                socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress
                            ("10.0.2.2",9999),5000);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    writer = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream(),"utf-8")),true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Log.i("TAG","connect to server");
                //尝试接受服务端信息
                try {
                    jsonString = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //解析服务器信息
                if(jsonString != null) {
                    try {
                        jsonObject = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.i("Online","Received from server"+jsonObject.toString());
                    try {
                        code = (int) jsonObject.get("code");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    switch (code) {
                        case 108:
                            stage = 2;
                            break;
                        case 308:
                            break;
                        case 408:
                            stage = 3;
                            break;
                        case 608:
                            try {
                                score = (int) jsonObject.get("score");
                                gameover = (boolean) jsonObject.get("gameover");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            easyGame.setEnemy(score,gameover);
                            if(gameover && easyGame.getGameOverFlag()) {
                                stage = 4;
                            }
                            break;
                        case 808:
                            try {
                                enemyLastScore = (int) jsonObject.get("score");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            easyGame.setEnemy(enemyLastScore,true);
                            TextView view = findViewById(R.id.your_score);
                            view.setText(lastScore);
                            view = findViewById(R.id.enemy_score);
                            view.setText(enemyLastScore);
                            setContentView(R.layout.online_record);


                            break;
                        default:
                            break;
                    }
                }




                //发送数据相关赋值
                uuid = easyGame.getUUID();
                score = easyGame.getScore();
                gameover = easyGame.getGameOverFlag();
                jsonObject = new JSONObject();
                //发送的数据

                switch (stage) {
                    case 1:
                        try {
                            jsonObject.put("code",8);
                            jsonObject.put("uuid",uuid);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 2:
                        try {
                            jsonObject.put("code",208);
                            jsonObject.put("uuid",uuid);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 3:
                        try {
                            jsonObject.put("code",508);
                            jsonObject.put("uuid",uuid);
                            jsonObject.put("score",score);
                            jsonObject.put("gameover",gameover);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 4:
                        try {
                            jsonObject.put("code",708);
                            jsonObject.put("uuid",uuid);
                            jsonObject.put("score",lastScore);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        break;
                }
                jsonString = jsonObject.toString();
                writer.println(jsonString);
            };
            executorService.scheduleAtFixedRate(tack,0,1, TimeUnit.SECONDS);

        }
    }



}
