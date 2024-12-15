package tasks;

import common.Area;
import common.Person;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
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

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    //мапа соотнесения id региона с его именем
    //создана чтобы быстрее находить имена регионов, не обращаясь к методу
    var areaIdToName = areas
        .stream()
        .collect(toMap(Area::getId, Area::getName));

    return persons
        .stream()
        .flatMap(person -> personAreaIds.get(person.id())
            .stream()
            .map(regionId -> person.firstName() + " - " + areaIdToName.get(regionId))
        )
        .collect(Collectors.toSet());
  }
}
