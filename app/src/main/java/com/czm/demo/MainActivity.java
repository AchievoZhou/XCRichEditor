package com.czm.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.czm.richeditor.R;
import com.czm.xcricheditor.EditItem;
import com.czm.xcricheditor.XCRichEditor;
import com.example.localalbum.common.ImageUtils;
import com.example.localalbum.common.LocalImageHelper;
import com.example.localalbum.ui.LocalAlbum;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private XCRichEditor mRichEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        findViewById(R.id.add_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPic();
            }
        });
        mRichEditor = (XCRichEditor) findViewById(R.id.richEditor);
    }

    private void addPic() {
        Intent intent = new Intent(MainActivity.this, LocalAlbum.class);
        startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("czm","requestCode="+requestCode);
//        Toast.makeText(getApplicationContext(),"requestCode="+requestCode,Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                if (LocalImageHelper.getInstance().isResultOk()) {
                    LocalImageHelper.getInstance().setResultOk(false);
                    //获取选中的图片
                    List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
                    Log.e("czm","file size="+files.size());
                    List<EditItem> items = new ArrayList<>();
                    for (int i = 0; i < files.size(); i++) {
                        Log.e("czm","file uri="+files.get(i).getOriginalUri());
                        Log.e("czm","file getThumbnailUri="+files.get(i).getThumbnailUri());
                        EditItem item = new EditItem();
                        item.setUri(Uri.parse(files.get(i).getOriginalUri()));
                        item.setType(1);
                        item.setContent(files.get(i).getOriginalUri());
                        items.add(item);
                    }
                    mRichEditor.addImage(items);
                    //清空选中的图片
                    files.clear();
                    //设置当前选中的图片数量
                    LocalImageHelper.getInstance().setCurrentSize(files.size());
                }
                //清空选中的图片
                LocalImageHelper.getInstance().getCheckedItems().clear();
                break;
            default:
                break;
        }
    }
}
