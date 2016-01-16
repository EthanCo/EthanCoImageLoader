# EthanCoImageLoader #

仿照ImageLoader，采用内存、本地存储、网络三级缓存，实现图片的异步加载。  


- 采用责任链模式，将对图片的处理连成一条链，沿着这条链传递请求，直到获得图片为止。  
- 异步加载通过线程池实现，并对图片进行了压缩处理，有效避免了OOM，提高Bitmap加载时的性能。  
- 数据的缓存采用LruCache、DiskLruCache。避免了流量的浪费及OOM的发生。
![EthanCoImageLoader](/EthanCoImageLoader.gif)
 
