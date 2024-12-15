package tasks;

import common.Person;
import common.PersonService;

import java.util.*;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    return personService
        .findPersons(personIds)
        .stream()
        .sorted(Comparator.comparing(elem -> personIds.indexOf(elem.id())))
        .toList();

    /*
     * оценка асимптотики работы:
     * время работы: в худшем случае O(n) из-за indexOf, в лучшем O(log n) (сортировка) (операции stream и toList работают за O(1)
     * эта оценка без учета времени сервиса, тк его реализация неизвестна
     * */
  }
}
