package com.example.tired;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Generate extends AppCompatActivity {
    private EditText textInput;
    private RadioGroup radioGroup;
    private RadioButton radioButtonQR;
    private RadioButton radioButtonBarcode;
  //  private Button generateButton;
    private ImageView codeImageView;
    private TextView contextTextView;
    private TextView typeTextView;
    private TextView contextTextView1;
    private TextView typeTextView1;
    private ImageButton downloadButton,clearButton,shareButton,generateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{getResources().getColor(R.color.customActionBarColor),
                        getResources().getColor(R.color.customActionBarColorDark)}));
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_generate);


        textInput = findViewById(R.id.textInput);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonQR = findViewById(R.id.radioButtonQR);
        radioButtonBarcode = findViewById(R.id.radioButtonBarcode);
        generateButton = findViewById(R.id.generateButton);
        codeImageView = findViewById(R.id.codeImageView);
        downloadButton = findViewById(R.id.downloadButton);
        shareButton = findViewById(R.id.shareButton);
        clearButton = findViewById(R.id.clearButton);
        contextTextView = findViewById(R.id.contextTextView1);
        typeTextView = findViewById(R.id.typeTextView1);
        contextTextView1 = findViewById(R.id.contextTextView12);
        typeTextView1 = findViewById(R.id.typeTextView12);




        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle download button click
                downloadImage();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle share button click
                shareImage();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle clear button click
                clearImage();
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCode();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateCode() {
        String text = textInput.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioButtonQR.isChecked()) {
            generateQRCode(text);
            contextTextView.setText(text);
            typeTextView.setText("QR Code");
        } else if (radioButtonBarcode.isChecked()) {
            generateBarcode(text);
            contextTextView.setText(text);
            typeTextView.setText("Barcode");
        }

        downloadButton.setVisibility(View.VISIBLE);
        shareButton.setVisibility(View.VISIBLE);
        clearButton.setVisibility(View.VISIBLE);
        typeTextView.setVisibility(View.VISIBLE);
        contextTextView.setVisibility(View.VISIBLE);
        typeTextView1.setVisibility(View.VISIBLE);
        contextTextView1.setVisibility(View.VISIBLE);
    }


    private void generateQRCode(String text) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 500, 500, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            codeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateBarcode(String text) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, 500, 200);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            codeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating barcode", Toast.LENGTH_SHORT).show();
        }
    }
    private void downloadImage() {
        // Get the drawable from the ImageView
        Drawable drawable = codeImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Save the bitmap to a file in the device's Downloads folder
            String fileName = "QRCodeImage_" + System.currentTimeMillis() + ".jpg";
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(directory, fileName);

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // Notify the MediaScanner to scan the saved image file
                MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);

                Toast.makeText(this, "Image Saved In Downloads", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error downloading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearImage() {
        codeImageView.setImageBitmap(null);
        textInput.setText("");
        downloadButton.setVisibility(View.GONE);
        shareButton.setVisibility(View.GONE);
        clearButton.setVisibility(View.GONE);
        contextTextView.setText("");
        typeTextView.setText("");
        contextTextView1.setText("");
        typeTextView1.setText("");
    }
    private void shareImage() {
        // Get the drawable from the ImageView
        Drawable drawable = codeImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Share the bitmap using an Intent
            try {
                String fileName = "QRCodeImage.jpg";

                // Save the bitmap to a temporary file
                File cachePath = new File(getCacheDir(), "images");
                cachePath.mkdirs();
                File imageFile = new File(cachePath, fileName);
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();

                // Create an intent to share the image
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", imageFile);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission to receiving apps
                startActivity(Intent.createChooser(shareIntent, "Share QR Code"));

                Toast.makeText(this, "Share Image", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error sharing image", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(this, "File provider configuration error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
