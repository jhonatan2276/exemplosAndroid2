package br.edu.unidavi.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button buttonPicture;
    private ImageView captureImagem;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPicture = findViewById(R.id.button_picture);
        captureImagem = findViewById(R.id.img_picture);
        buttonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg");
                Uri outputDir = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID, file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputDir);

                startActivityForResult(intent, 1_000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1_000) {
            if (data != null && data.hasExtra("data")) {
                Bitmap thumbnail = data.getParcelableExtra("data");
                captureImagem.setImageBitmap(thumbnail);
            } else {
                int width = captureImagem.getWidth();
                int height = captureImagem.getHeight();

                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                factoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), factoryOptions);

                int imageWidth = factoryOptions.outWidth;
                int imageHeight = factoryOptions.outHeight;

                //Verifica o quanto precisams escalar a imagem
                int scaleFactor = Math.min(imageWidth / width, imageHeight / height);

                factoryOptions.inJustDecodeBounds = false;
                factoryOptions.inSampleSize = scaleFactor;

                Bitmap image = BitmapFactory.decodeFile(file.getPath(), factoryOptions);
                captureImagem.setImageBitmap(image);
            }
        }
    }
}
