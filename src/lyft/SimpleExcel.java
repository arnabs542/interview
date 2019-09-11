package lyft;

/**
 * 设计excel, A1=B1+C1, B1=D1+1这种情况如何处理
 * 是A=B+C或A=B-C的格式，支持+-两种就行了，主要是这里B和C可以是常数或者表达式的，A必须是表达式
 * 如果当前的一个表达式，比如A=B+C，造成冲突的话，则直接ignore这个表达式。处理方法的话是topological sorting
 */
public class SimpleExcel {
}
