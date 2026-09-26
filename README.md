# QA Selenium Capstone

Capstone-проєкт курсу QA Mentorship: фреймворк на **Page Object** + **5 тестів**, які мусять
стати зеленими. Приймання — **зелений прогін GitHub Actions** у твоєму форку (не слова, не скріншот).

## Як працювати

```bash
# 1. Форкни репозиторій і клонуй свій форк
git clone https://github.com/<твій-логін>/qa-selenium-capstone.git
cd qa-selenium-capstone

# 2. Подивись, що саме вимагається (методи з TODO)
grep -rn "TODO" src/test/java | head -20

# 3. Реалізуй Page Object-и, поки тести не стануть зеленими
#    (локально: mvn test, якщо в тебе є JDK 17 і Chrome)

# 4. Запуш і відкрий Actions — коли прогін зелений, прикріп його посилання в курсі
git push origin main
```

## Що перевіряє CI

`mvn -B -q test` на Ubuntu з Java 17 і Chrome у headless-режимі: 5 тестів проти публічного
стенду Swag Labs (`https://www.saucedemo.com/`, відкритий демо-застосунок Sauce Labs).

## Вимоги до фреймворку

- Page Object: локатори — у константах класу, тести не знають про селектори.
- Жодних `Thread.sleep`: тільки `WebDriverWait`/`ExpectedConditions`.
- Тести незалежні один від одного (кожен сам логіниться).
- Fluent-методи Page Object-ів (повертають `this` або наступну сторінку).

## Ліцензія

MIT. Скелет створено для цього курсу; сторонні фреймворки не копіювались.
