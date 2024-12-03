package com.example.doggleit;
import  org.tensorflow.lite.support.image.TensorImage;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.Manifest;

import com.example.doggleit.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public class MainActivity extends AppCompatActivity {

    Button selectBtn, captureBtn ,predictBtn;
    ImageView imageView;
    TextView result;
    private Bitmap image;

    int IMG_SIZE=224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        selectBtn = findViewById(R.id.selectBtn);
        captureBtn=findViewById(R.id.captureBtn);
        predictBtn=findViewById(R.id.predictBtn);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }

        });
        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image != null) {
                    classifyImage(image);
                } else {
                    result.setText("No Image Detected");
                }
            }
        });

    }
    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[IMG_SIZE * IMG_SIZE];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < IMG_SIZE; i ++){
                for(int j = 0; j < IMG_SIZE; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            int max = getMax(confidences);
            String[] classes = {"Affenpinscher","Afghan Hound","African Hunting Dog","Airedale","American Staffordshire Terrier","Appenzeller","Australian Terrieer","Basenji","Basset","Beagle","Bedlington Terrier","Bernese Mountain Dog","Black-and-tan Coonhound","Blenheim Spaniel","Bloodhound","Bluetick","Border Collie","Border Terrier","Borzoi","Boston Bull","Bouvier Des Flandres","Boxer","Brabancon Griffon","Briard","Brittany Spaniel","Bull Mastiff","Cairn","Cardigan","Chesapeake Bay Retriever","Chihuahua","Chow","Clumber","Cocker Spaniel","Collie","Curly-coated Retriever","Dandie Dinmont","Dhole","Dingo","Doberman","English Foxhound","English Setter","English Springer","Entlebucher","Eskimo Dog","Flat-coated Retriever","French Bulldog","German Shepherd","German Short-haired Pointer","Giant Schnauzer","Golden Retriever","Gordon Setter","Great Dane","Great Pyrenees","Greater Swiss Mountain Dog","Groenendael","Ibizan Hound","Irish Setter","Irish Terrier","Irish Water Spaniel","Irish Wolfhound","Italian Greyhound","Japanese Spaniel","Keeshond","Kelpie","Kerry Blue Terrier","Komondor","Kuvasz","Labrador Retriever","Lakeland Terrier","Leonberg","Lhasa","Malamute","Malinois","Maltese Dog","Mexican Hairless","Miniature Pinscher","Miniature Poodle","Miniature Schnauzer","Newfoundland","Norfolk Terrier","Norwegian Elkhound","Norwich Terrier","Old English Sheepdog","Otterhound","Papillon","Pekinese","Pembroke","Pomeranian","Pug","Redbone","Rhodesian Ridgeback","Rottweiler","Saint Bernard","Saluki","Samoyed","Schipperke","Scotch Terrier","Scottish Deerhound","Sealyham Terrier","Shetland Sheepdog","Shih-tzu","Siberian Husky","Silky Terrier","Soft-coated Wheaten Terrier","Staffordshire Bullterrier","Standard Poodle","Standard Schnauzer","Sussex Spaniel","Tibetan Mastiff","Tibetan Terrier","Toy Poodle","Toy Terrier","Vizsla","Walker Hound","Weimaraner","Welsh Springer Spaniel","West Highland White Terrier","Whippet","Wire-haired Fox Terrier","Yorkshire Terrier"};

            result.setText("The Breed of this friend is : "+classes[max]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }



    int getMax(float[] arr){
        int max=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]>arr[max])max=i;
        }
        return max;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, IMG_SIZE, IMG_SIZE, false);
//                classifyImage(image);
            }else{
                Uri dat = data.getData();
                image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, IMG_SIZE, IMG_SIZE, false);
//                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}