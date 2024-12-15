package tasks;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/*
  Еще один вариант задачи обогащения
  На вход имеем коллекцию персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе хотим получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    //мапа соотносящая id персоны и список резюме для этой персоны
    //используется чтобы быстро и удобно получать список резюме при знании id
    var personIdToResumes = personService.findResumes(persons.stream().map(Person::id).collect(toSet()))
        .stream()
        .collect(Collectors.groupingBy(Resume::personId, toSet()));

    return persons
        .stream()
        // getOrDefault используется на случай если у персоны нет резюме
        // сам метод подсказала идея как упрощение containsKey
        .map(person -> new PersonWithResumes(person, personIdToResumes.getOrDefault(person.id(), Set.of())))
        .collect(toSet());
  }
}
