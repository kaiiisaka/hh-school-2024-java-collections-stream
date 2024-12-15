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

    //вместо дополнительного кода тернарный оператор
    return persons.isEmpty()
        ? Collections.emptyList()
        : persons
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
     * Set<Integer> set создается уже отсортированным внутри
     * это происходит из-за того, что HashSet при создании задает ключи не рандомно, а отконстантного new Object()
     * в его конструктор можно положить что угодно при создании (любой порядок чисел) -
     * он все равно будет отсортирован:
     * List<Integer> random = List.of(8, 3, 5, 4, 1, 9, 2, 10, 7, 6);
     * Set<Integer> randomSet = new HashSet<>(random);
     * randomSet тоже будет отсортирован и при сравнении Objects.equals(snapshot.toString(), randomSet.toString());
     * тоже вернул бы true (если сократить количество элементов до 10)
     */
  }
}
