# hw1解释

hw1的目标为实现一个搜索框的功能，涉及到了3个`.java`文件和3个`.xml`文件，这3个`.xml`文件都为layout服务。

#### `.java`文件

1. `MainActivity.java`：程序主界面
2. `MyAdapter.java`：`RecyclerView.Adapter`的继承类，服务于`RecyclerView`
3. `InsideActivity.java`：点入后的界面

#### `.xml`文件

1. `activity_main.xml`：程序主界面
2. `view_rv_item.xml`：程序主界面中的每一行文字
3. `activity_inside.xml`：点入后的界面

## 文件一：`MainActivity.java`

`MainActivity.java`有点类似于`c++`中的源文件，里边包含了`MainActivity`类，有些类似于`c++`的`main`函数。在`MainActivity`类中，定义了一些变量，和函数。`MainActivity`中最重要的函数是`onCreate()`函数，是本app运行时第一个调用的函数（在本hw1手写部分中）。

## `onCreate()`函数

`onCreate()`函数主要完成三个任务：

1. 绑定主界面`.xml`文件
2. 得到每一行的文字，并存入Array
3. 绑定recyclerView，并且将每一行的文字传入recyclerView
4. 处理搜索框。方法是获取搜索框中的内容，然后搜索含有搜索框内容的文本。

## 文件二：`MyAdapter.java`

`MyAdapter.java`文件主要服务于recyclerView。要实现recyclerView，必须重写它的Adapter类。`MyAdapter.java`中定义了`updateData()`函数，是用来更新显示内容的。`MyAdapter.java`中有还有很多其他函数，是用来封装显示多行“这是第i行”的。`MyAdapter.java`中还定义了一个类`ViewHolder`，用来绑定`view_rv_item.xml`，即为每一行“这是第i行”。

## 文件三：`InsideActivity.java`

`InsideActivity.java`绑定了内部界面，也就是点这是第i行后显示的界面。

