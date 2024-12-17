package tasks;

import common.Area;
import common.Person;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  //тут вынес метод сложения строк
  private static String concatPersonWithArea(Person person, Area area) {
    return person.firstName() + " - " + area.getName();
  }

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    //мапа соотнесения id арейки с её экземпляром
    Map<Integer, Area> areaIdToName = areas
        .stream()
        .collect(toMap(Area::getId, Function.identity()));

    return persons
        .stream()
        .flatMap(person -> personAreaIds.get(person.id())
            .stream()
            .map(areaId -> concatPersonWithArea(person, areaIdToName.get(areaId)))
        )
        .collect(Collectors.toSet());
  }
}
