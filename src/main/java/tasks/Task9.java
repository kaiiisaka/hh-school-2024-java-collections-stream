package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {

    //упростил код и добавил стрим
    return persons
        .stream()
        .skip(1) //вместо persons.remove(0) - всё теперь делается в стриме
        .map(Person::firstName)
        .toList(); //код теперь разделен, построчно читать удобнее
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
    /*
     * Тут заменяется вообще вся конструкция: так как возвращаемое значение это Set строк,
     * я удалил distinct(). после этого осталось .stream().collect(Collectors.toSet()) -
     * запись, где вообще стрим не нужен, поэтому остается только конструктор сета из persons
     */
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {

    //заменяем несколько операций одним стримом с конкатенацией not null отфильтрованных строк
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {

    //старая логика заменена на работу со стримами и убрана лишняя переменная
    //(у которой был указан лишний начальный размер)
    return persons
        .stream()
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toMap(Person::id, this::convertPersonToString));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons2.stream().anyMatch(persons1::contains);
    // anyMatch возвращает true если в persons2 содержится такой же элемент как в persons1
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
    //убрали переменную count и заменили ее на использование метода count()
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
    /*
     * Внутри HashSet лежит HashMap. HashMap при создании распределяет значения по бакетам
     * в соответствии с их хеш-кодом. у Integer хеш-код совпадает с самим числом.
     * по дефолту сначала 16 бакетов. когда количество значений в сете ≥ 16 * 0.75 - количество бакетов увеличивается
     * по этой причине если создать сет из чисел, которые идет по порядку они будут в сете по порядку
     * даже если их перемешали (как в примере) - для каждого из них будет свой бакет.
     * Если же создавать сет из чисел, значения которых превышают текущее количество бакетов,
     * эти числа не будут идти по порядку (например, как в комментарии к PR этой задачи)
     * если в первые 12 значений сета добавить число 33 - оно будет распределено хеш-функцией в случайный бакет,
     * так как 33-го бакета просто еще не существует.
     * А если добавлять число 33 в сет когда количество бакетов будет уже 64 - число 33 будет уже идти по порядку.
     * Итог: assert верен всегда из-за того, что внутри себя HasSet распределяет значения Integer`ов по бакетам
     * в соответствии с их значением и количеством этих бакетов.
     * */
  }
}
