# krogue
krogue is a roguelike game written in kotlin

Authors: Kirill Mitkin, Andrey Khorokhorin

### Диаграмма классов проекта

![Class diagram](./uml/rendered/class_diagram.png)


### Диаграмма компонентов

Связь компонентов в системе укладывается в стандартный паттерн Model-View-Controller.
* Компонент модели состоит из 3 основных компонентов: карты, игрока и мобов.
* Контроллер включает в себя очередь событий, непосредственно изменяющие состояние модели, и общается через линии логов
с основным логирующим классом Logger
* View состоит из 3 основых View -- PlayView, StartView и LoseView, где PlayView изменяет состояние системы 
посредством паттерном команда в api Controller 

![Component_diagram](./uml/rendered/component_diagram.png)
