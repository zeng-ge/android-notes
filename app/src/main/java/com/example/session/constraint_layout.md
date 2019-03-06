# Constraint Layout

  约束布局通过设置与父元素和兄弟元素之间的关系来实现布局。  
  有点类似于RelativeLayout，但是功能更强大
  
## 位置关系

  决定当前元素与目标元素的位置关系
  
  位置重叠时，可以用`android:elevation="2dp"`来调整z-index
  

  - layout_constraintLeft_toLeftOf              左边对齐
  - layout_constraintLeft_toRightOf             左边与目标的右边对齐
  - layout_constraintRight_toLeftOf             右边与目标的左边对齐
  - layout_constraintRight_toRightOf            右边边对齐
  - layout_constraintTop_toTopOf                上边对齐
  - layout_constraintTop_toBottomOf             上边与目标的下边对齐
  - layout_constraintBottom_toTopOf             下边与目标的上边对齐
  - layout_constraintBottom_toBottomOf          下边对齐
  - layout_constraintBaseline_toBaselineOf      基线对齐
  - layout_constraintStart_toEndOf              开始边与目标的结束边对齐
  - layout_constraintStart_toStartOf            开始边对齐
  - layout_constraintEnd_toStartOf              结束边与目标的开始边对齐
  - layout_constraintEnd_toEndOf                结束边对齐
  
  后面4个start与end的关系，是针对LTR的情况，有些时间左右会发生变化
  
  <img src="https://developer.android.com/reference/android/support/constraint/resources/images/chains-styles.png" width="400" />
    
## chain
  多个元素之间可以相互形成约束，进而形成链  
  如下例：
  1. buttion3左边与parent的左边相接
  2. button3的右边与button4右边相接
  3. button4的左边与button3的右边相接
  4. button4的右边与button3的左边相接
  5. button5的左边与button4的右边相接
  6. button5右边与parent的右边相接
  即button3、button4、button5是双向关联的
  ```
  <Button
      android:id="@+id/button3"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginStart="16dp"
      android:layout_marginBottom="16dp"
      android:background="@android:color/holo_red_light"
      android:text="Button"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintEnd_toStartOf="@+id/button4"
      app:layout_constraintHorizontal_chainStyle="spread_inside"
      app:layout_constraintHorizontal_weight="3"
      app:layout_constraintStart_toStartOf="parent" />

  <Button
      android:id="@+id/button4"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginBottom="16dp"
      android:background="@android:color/holo_green_light"
      android:text="Button"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintEnd_toStartOf="@+id/button5"
      app:layout_constraintHorizontal_weight="2"
      app:layout_constraintStart_toEndOf="@+id/button3" />

  <Button
      android:id="@+id/button5"
      android:layout_width="120dp"
      android:layout_height="0dp"
      android:layout_marginEnd="16dp"
      android:layout_marginBottom="16dp"
      android:background="@android:color/holo_orange_light"
      android:text="Button"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintDimensionRatio="2:1"
      app:layout_constraintEnd_toEndOf="parent"
      app:layout_constraintStart_toEndOf="@+id/button4" />

  ```

### app:layout_constraintHorizontal_weight
  形成chain的元素，如果宽为0`(match_constraint)`，它会占用父元素的多余空间  
  weight用来设置权重，类似于flex-grow的作用


### app:layout_constraintHorizontal_chainStyle
 
  为chaun的元素们设置处理多余空间的策略
  类似于flex里面的justfy-content，决定主轴上的空间分配
  配置项：
  1. **spread_inside**  类似于space around, 首尾也有一点空间
  2. **spread**         类似于space between，两端对齐
  3. **packed**         类似于center，全部挤在一起
  
  前置条件：  
  要有多余的空间，否则没有  
  如果chain中的某一个元素是match_constraint，即宽或高为0时，  
  此时多余的空间被占领了, chainStyle无法生效
  

### app:layout_constraintHorizontal_bias
  
  调整水平方向轴线比例，默认是0.5，即轴线在水平位置的center  
  如果想元素移到偏左的位置，可以设成0.2
  
  垂直方向:  
  **app:layout_constraintVertical_bias**  
  `app:layout_constraintHorizontal_bias="0.2"`

### app:layout_constraintDimensionRatio="2:1"
  设置元素水平宽的比例  
  以上面的button5为例，宽度为120dp，高度在计算后为60dp  
  计算的前提是，宽或高有一个是不定的，即为match_constraint
  