package com.example.ar;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private String MODEL_URL = "Wolves.gltf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.uxform);
        setUpModel();
        setUpPlane();

    }

    private void setUpPlane() {

        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));

    }

    private void createModel(AnchorNode anchorNode) {

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();

    }

    private void setUpModel() {

        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(
                                this, Uri.parse(MODEL_URL),
                                RenderableSource.SourceType.GLTF2)
                        .setScale(0.75f)
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId(MODEL_URL)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable )
                .exceptionally(throwable -> {
                    Log.d("ASHISH", "Can't Load Model");
                    return null;
                });

    }
}