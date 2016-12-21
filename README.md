# CustomWaveView
Android：实现自定义波浪view，可以直接在布局xml文件中设置波浪颜色、波浪缓慢湍急程度。现在很多app都有用到波浪view视图

# 原理图
水平线上定义5个点，同时外加4个辅助点，起点位置从PointF1开始
```
//画波形
mPath.moveTo(pointF1.x, pointF1.y);
mPath.quadTo(ctrF1.x, ctrF1.y, pointF2.x, pointF2.y);
mPath.quadTo(ctrF2.x, ctrF2.y, pointF3.x, pointF3.y);
mPath.quadTo(ctrF3.x, ctrF3.y, pointF4.x, pointF4.y);
mPath.quadTo(ctrF4.x, ctrF4.y, pointF5.x, pointF5.y);

//波形封闭
mPath.lineTo(pointF5.x, mHeight);
mPath.lineTo(pointF1.x, mHeight);
mPath.lineTo(pointF1.x, pointF1.y);
```
![princle](https://github.com/Ericsongyl/CustomWaveView/blob/master/princle.jpg)

# 效果图
![effect map](https://github.com/Ericsongyl/CustomWaveView/blob/master/GIF.gif)
