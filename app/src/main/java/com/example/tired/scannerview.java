package com.example.tired;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class scannerview extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 102;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 103;

    private CodeScanner mCodeScanner;
    private ImageView mImageView,changeacti,anotherImageButton;
    private ImageButton mSelectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannerview);
        getSupportActionBar().hide();
        mImageView = findViewById(R.id.image_view);
        changeacti = findViewById(R.id.previous_activity_button);
        mSelectImageButton = findViewById(R.id.select_image_button);
         anotherImageButton = findViewById(R.id.another_image_button);
        anotherImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultPopup("Tthe purpose and functionality of the camera view, which is used for scanning QR codes and barcodes. It helps users, especially those with visual impairments, understand the purpose of the camera view and navigate through the app using assistive technologies.");
            }
        });
        changeacti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ouroption.class));
                finish();
            }
        });

        mSelectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        // Check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is granted, initialize the CodeScanner
            initializeCodeScanner(scannerView);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, initialize CodeScanner
                CodeScannerView scannerView = findViewById(R.id.scanner_view);
                initializeCodeScanner(scannerView);
            } else {
                // Camera permission denied, show a message or take appropriate action
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showResultPopup1(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(scannerview.this);
                builder.setTitle("Popup Title");
                builder.setMessage(message);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle OK button click if needed
                    }
                });

                builder.show();
            }
        });
    }

    private void initializeCodeScanner(CodeScannerView scannerView) {
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showResultPopup(result.getText());
                        // Pause the CodeScanner after decoding
                        mCodeScanner.stopPreview();
                    }
                });
            }
        });
    }

    private void showResultPopup(final String resultText) {
        mCodeScanner.stopPreview();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scanned Result");
        builder.setMessage(resultText);
        builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Copy the result to clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Scanned Result", resultText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(scannerview.this, "Result copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        // Check if the result is a valid URL
        boolean isUrl = Patterns.WEB_URL.matcher(resultText).matches();
        if (isUrl) {
            builder.setNegativeButton("Open in Chrome", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Open the URL in Chrome
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(resultText));
                    startActivity(intent);
                }
            });
        }

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Resume the CodeScanner when cancel button is clicked
                mCodeScanner.startPreview();
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });
    }






    @Override
    protected void onResume() {
        super.onResume();
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    private void openImagePicker() {
        mCodeScanner.stopPreview();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                scanImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void scanImage(Bitmap bitmap) {
        mCodeScanner.stopPreview();
        // Configure barcode formats to be scanned
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128, BarcodeFormat.CODE_39);

        // Configure decoding hints
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);

        // Create an instance of the multi-format reader
        final MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);

        // Convert the bitmap image to binary pixels
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        HybridBinarizer binarizer = new HybridBinarizer(new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels));
        final com.google.zxing.BinaryBitmap binaryBitmap = new com.google.zxing.BinaryBitmap(binarizer);

        final Thread scanningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Decode the image using the multi-format reader
                    final Result result = multiFormatReader.decodeWithState(binaryBitmap);

                    // Handle the scanned result as needed (e.g., display in a popup)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResultPopup(result.getText());
                            mCodeScanner.stopPreview();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(scannerview.this, "Error scanning image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCodeScanner.startPreview(); // Resume CodeScanner preview
                        }
                    });
                }
            }
        });

        mCodeScanner.stopPreview(); // Pause CodeScanner preview when scanning from file
        scanningThread.start();
    }




}