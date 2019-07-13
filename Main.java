import java.util.Map;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
      ConcurrentFileSearch fileSearch = new ConcurrentFileSearch();
        Map<String,String> map =   fileSearch.findFileBFS("E:\\","exe"); // с точкой .exe найдет только файлы
        String mapToString = map.keySet().stream().map(key -> key + " in " + map.get(key)).collect(Collectors.joining("\n")); 
        System.out.println(mapToString);
        System.out.println(map.size());
        }
}
