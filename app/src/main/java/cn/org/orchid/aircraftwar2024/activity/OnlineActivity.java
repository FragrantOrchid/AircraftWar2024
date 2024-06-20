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
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
                Log.v("message", "OnlineActivityGetmessage");
                //该场比赛数据存入本地
                lastScore = (int) message.obj;
                Log.v("message","score is"+lastScore);
            } else if(message.what == 2) {
                enemyLastScore = (int)message.obj;
                Log.v("808","enemylastscore is "+enemyLastScore );
                //easyGame.setEnemy(enemyLastScore,true);
                setContentView(R.layout.online_record);
                Log.v("808","6");
                Log.v("808","1");
                TextView view = findViewById(R.id.your_score);
                Log.v("808","2");
                view.setText("你的成绩"+String.valueOf(lastScore)  );
                Log.v("808","3");
                view = findViewById(R.id.enemy_score);
                Log.v("808","4");
                view.setText("对手成绩"+String.valueOf(enemyLastScore)  );
                Log.v("808","5");

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        sound = getIntent().getBooleanExtra("sound",false);
        //在线游戏在这里直接启动
        Log.v("beginGame","beginGameInOnline");
        easyGame = new EasyGame(this,handler,sound);
        setContentView(easyGame);
        easyGame.pauseGame();
        new Thread(new ScoketThread(handler)).start();
    }





    //内部类完成通讯
    protected class ScoketThread extends Thread{
        private Handler handler;
        //private ScheduledExecutorService executorService;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter writer;
        //1:还未发包，2：发过包未匹配，3：已经匹配；
        private int stage;
        public ScoketThread(Handler handler) {
            this.handler = handler;
            //this.executorService = Executors.newScheduledThreadPool(1);
            stage = 1;
        }
        @Override
        public void run(){

                Log.v("Socket","SocketThreadRun");
                //数据相关变量声明


                //socket客户端实例化并连接
                socket = new Socket();

                try {
                    socket.connect(new InetSocketAddress
                            ("10.0.2.2",9999),1000);
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
                Log.i("Socket","connect to server");







                //尝试接受服务端信息
                Thread receiveServer = new Thread() {
                    @Override
                    public void run() {
                        int code;
                        UUID uuid;
                        int score;
                        boolean gameover;
                        String jsonString = null;
                        JSONObject jsonObject = null;
                        try{
                            while((jsonString = in.readLine())!=null)
                            {
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
                                            Log.v("OnlineActivity","108");
                                            stage = 2;
                                            break;
                                        case 308:
                                            Log.v("OnlineActivity","308");
                                            break;
                                        case 408:
                                            Log.v("OnlineActivity","408");
                                            stage = 3;
                                            easyGame.continueGame();
                                            break;
                                        case 608:
                                            Log.v("OnlineActivity","608");
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
                                            Log.v("808","receive 808");
                                            try {
                                                enemyLastScore = (int) jsonObject.get("score");
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }


                                            Message msg2 = Message.obtain();
                                            msg2.what = 2;
                                            msg2.obj = enemyLastScore;
                                            handler.sendMessage(msg2);






                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                };
                receiveServer.start();


                Thread sendThread = new Thread() {
                    @Override
                    public void run() {
                        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                        Runnable task = () -> {
                            //发送数据相关赋值
                            UUID uuid = easyGame.getUUID();
                            int score = easyGame.getScore();
                            boolean gameover = easyGame.getGameOverFlag();
                            JSONObject jsonObject = new JSONObject();
                            String jsonString = null;
                            //发送的数据

                            switch (stage) {
                                case 1:
                                    try {
                                        jsonObject.put("code", 8);
                                        jsonObject.put("uuid", uuid);
                                        Log.v("Socket", "Socket with 8");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case 2:
                                    try {
                                        jsonObject.put("code", 208);
                                        jsonObject.put("uuid", uuid);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case 3:
                                    try {
                                        jsonObject.put("code", 508);
                                        jsonObject.put("uuid", uuid);
                                        jsonObject.put("score", score);
                                        jsonObject.put("gameover", gameover);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                case 4:
                                    try {
                                        jsonObject.put("code", 708);
                                        jsonObject.put("uuid", uuid);
                                        jsonObject.put("score", lastScore);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                default:
                                    break;
                            }
                            jsonString = jsonObject.toString();
                            writer.println(jsonString);
                            Log.v("Online", "Send Once");
                        };
                        scheduler.scheduleWithFixedDelay(task, 1000, 1000, TimeUnit.MILLISECONDS);
                    }
                };
                sendThread.start();


                //定时发送



        }
    }



}
