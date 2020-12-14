import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        primaryStage.setTitle("World Of Trash");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

/*
    public static void music(){
        Media s= new Media("file///C://Users//acelu//Desktop//T4-1-GUI//ingameMusic.wav");
        //String s= "Ingame music.mp3";
        Media m = new Media(Paths.get(String.valueOf(s)).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(m);
        mediaPlayer.play();
    }

    public static void playMusic(String filepath) {
        InputStream music;
        try {
            music = new FileInputStream(new File(filepath));
            AudioStream a = new AudioStream(music);
            AudioPlayer.player.start(a);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }
*/

    public static void main(String[] args) {
        launch(args);

    }
}

