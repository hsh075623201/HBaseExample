import org.apache.hadoop.hbase.filter._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{TableName, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import scala.collection.JavaConverters._
/**
  * Created by admin on 2017/12/15.
  */
object HBaseTest {

  def main(args: Array[String]) {
    val config = HBaseConfiguration.create()
    config.set("hbase.zookeeper.quorum", "hadoop.master,hadoop.master2,hadoop.data1");
    config.set("hbase.zookeeper.property.clientPort", "2181")
    val connection = ConnectionFactory.createConnection(config)
    /*val admin = connection.getAdmin()
    admin.listTables().foreach(println)*/

    val table = connection.getTable(TableName.valueOf("month_user_tag"));
    /*getDataByKey(table)
    getDataByListKey(table)*/
    getDataByScan(table)
  }

  def getDataByScan(table: Table) ={

    //前缀过滤
    val filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL)
    val prefixFilter = new PrefixFilter("20170101_BD_2011".getBytes)
    filterList.addFilter(prefixFilter)
    //startrow 过滤
    val minRowFilter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL,new BinaryComparator("20170101_BD_201104000001".getBytes))
    filterList.addFilter(minRowFilter)
    //endrow过滤
    val maxRowFilter = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL,new BinaryComparator("20170101_BD_201106000259".getBytes))
    filterList.addFilter(maxRowFilter)

    val scan = new Scan()
    scan.setFilter(filterList)
    val resultScanner =  table.getScanner(scan)
    val result = resultScanner.asScala.map(ele=>{
      //println(new String(ele.value()))
      new String(ele.getValue(Bytes.toBytes("cf"),Bytes.toBytes("tag")))
    })

    println(result.size)
    println(result.mkString(","))


  }

  //单个key 查询
  def getDataByKey(table: Table) ={
    val params = new Get(Bytes.toBytes("20170101_BD_201104000001"))
    val resultSet = table.get(params)
    val result = resultSet.getValue(Bytes.toBytes("cf"),Bytes.toBytes("tag"))
    println(new String(result))
  }
  //list key 查询
  def getDataByListKey(table: Table) ={
    val listParams:java.util.List[Get] = new java.util.ArrayList[Get]()

    listParams.add(new Get(Bytes.toBytes("20170101_BD_201104000001")))
    listParams.add(new Get(Bytes.toBytes("20170101_BD_201106000259")))
    val resultSet = table.get(listParams)
    val result = resultSet.map(ele=>{
      new String(ele.getValue(Bytes.toBytes("cf"),Bytes.toBytes("tag")))
    }).mkString(",")
    println(result)
  }
}