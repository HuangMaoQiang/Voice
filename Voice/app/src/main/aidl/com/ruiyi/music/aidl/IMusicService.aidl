// ITingFMService.aidl
package com.ruiyi.music.aidl;

// Declare any non-default types here with import statements

interface IMusicService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

     boolean playPre();//上一首 返回false 表示列表已经到顶了
     boolean playNext();//下一首 返回false 表示列表已经到底了
     boolean playStart();//开始播放
     boolean playPause();//停止或暂停播放

     boolean playFinish();//退出整个应用

     boolean setSearchTracks(String txt,boolean isPlay);//搜素关键字

     boolean seletePrePage();//上一页
     boolean seleteNextPage();//下一页
     boolean onChangePage();//换一页或者换一批
     boolean seleteIndex(int index);//第几个

     boolean setVolume(float left, float right);//控制音量

}
