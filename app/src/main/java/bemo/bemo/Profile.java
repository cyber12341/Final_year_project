package bemo.bemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bemo.bemo.Common.Common;

public class Profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    CollapsingToolbarLayout collapsingToolbar;
    Button btnLogout;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    MenuItem saveItem, editItem;
    ImageView photoProfile;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private EditText edtName, edtNohp, edtEmail;

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindActivity();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.main_profile);
        mToolbar.setOnMenuItemClickListener(this);
        saveItem = mToolbar.getMenu().findItem(R.id.edit_save);
        editItem = mToolbar.getMenu().findItem(R.id.edit_profile);
        photoProfile = findViewById(R.id.profilePhoto);

        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        photoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        if (Common.currentuser.getAvatarUrl() != null
                && !TextUtils.isEmpty(Common.currentuser.getAvatarUrl())) {
            Glide.with(this).load(Common.currentuser.getAvatarUrl()).asBitmap().centerCrop()
                    .error(R.drawable.ic_launcher_round).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(photoProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            photoProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }


        edtName.setText(Common.currentuser.getName());
        edtEmail.setText(Common.currentuser.getEmail());
        edtNohp.setText(Common.currentuser.getPhone());

        btnLogout.setOnClickListener(this);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture : "), Common.PICK_IMAGE_REQUEST);
    }

    private void editData() {
        Map<String, Object> editdata = new HashMap<>();
        editdata.put("name", edtName.getText().toString());
        editdata.put("email", edtEmail.getText().toString());
        editdata.put("phone", edtNohp.getText().toString());
        Log.e("tag", editdata.toString());
        DatabaseReference driverInformations = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
        driverInformations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(editdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Edited !", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(Profile.this, "Edit Failed !", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri saveUri = data.getData();
            if (saveUri != null) {
                final ProgressDialog mDialog = new ProgressDialog(this);
                mDialog.setMessage("Uploading...");
                mDialog.show();

                String imageName = UUID.randomUUID().toString();
                final StorageReference imagefolder = storageReference.child("image/" + imageName);
                imagefolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDialog.dismiss();

                        imagefolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String, Object> avatarUpdate = new HashMap<>();
                                avatarUpdate.put("avatarUrl", uri.toString());

                                DatabaseReference driverInformations = FirebaseDatabase.getInstance().getReference(Common.user_driver_tbl);
                                driverInformations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .updateChildren(avatarUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Profile.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(Profile.this, "Upload Failed !", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mDialog.setMessage("Uploaded " + progress + "%");
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_profile, menu);
       /* saveItem = menu.findItem(R.id.edit_save);
        editItem = menu.findItem(R.id.edit_profile);
        saveItem.setVisible(false);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                edtName.setEnabled(true);
                edtEmail.setEnabled(true);
                edtNohp.setEnabled(true);
                saveItem.setVisible(true);
                editItem.setVisible(false);
                break;

            case R.id.edit_save:

                edtName.setEnabled(false);
                edtEmail.setEnabled(false);
                edtNohp.setEnabled(false);
                saveItem.setVisible(false);
                editItem.setVisible(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindActivity() {
        mToolbar = findViewById(R.id.main_toolbar);
        mTitle = findViewById(R.id.main_textview_title);
        mTitleContainer = findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = findViewById(R.id.app_bar);
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtNohp = findViewById(R.id.edtHp);
        btnLogout = findViewById(R.id.btn_logout);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);

    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
                mTitle.setText("name");
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
                mTitle.setText("");
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile:
                edtName.setEnabled(true);
                edtEmail.setEnabled(true);
                edtNohp.setEnabled(true);
                saveItem.setVisible(true);
                editItem.setVisible(false);
                break;

            case R.id.edit_save:
                Log.e("asd", "asdasd");
                editData();
                edtName.setEnabled(false);
                edtEmail.setEnabled(false);
                edtNohp.setEnabled(false);
                saveItem.setVisible(false);
                editItem.setVisible(true);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                auth.signOut();
                Intent i = new Intent(Profile.this, Login.class);
                startActivity(i);
                finishAffinity();
                finish();
                break;
        }

    }
}
