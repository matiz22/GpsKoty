from enum import Enum
import itertools
import math
import random


AREA = 5000, 5000  # cm
HOME = AREA[0] / 2, AREA[1] / 2

class PreyType(Enum):
    FIELD_MOUSE=0
    HOUSE_MOUSE=1
    SNAIL=2
    LEAF=4
    ROCK=5

class CatType(Enum):
    LUNA=0,
    ADRIANA=1
    DANTE=2


class Prey:
    def __init__(self, type: PreyType, position: tuple[int, int]):
        self.type = type
        self.x, self.y = position
    
    def __repr__(self) -> str:
        return f'Prey(type={self.type}, pos=({self.position}))'
    
    @property
    def position(self):
        return self.x, self.y
    
    @property
    def time_to_hunt_down(self) -> int:
        times = {
            PreyType.FIELD_MOUSE:   3 * 60,
            PreyType.HOUSE_MOUSE:   2 * 60,
            PreyType.SNAIL:         1.5 * 60,
            PreyType.ROCK:          0.5 * 60,
            PreyType.LEAF:          1 * 60,
        }
        return int(times[self.type])
    
    @staticmethod
    def get_preys():
        preys = (
            150     # field mouse
            + 80    # house mouse
            + 90    # snail
            + 300   # leaf
            + 200   # rock
        )

        positions = [
            (i, j) 
            for i in range(0, AREA[0], 5) for j in range(0, AREA[1], 5)
            if (i, j) != HOME
        ]

        prey_positions = random.sample(positions, preys)

        # # save positions
        # open('data.json', 'w').write(json.dumps(prey_positions))
        # exit()
        # prey_positions = json.loads(open('./data.json', 'r').read())

        field_mice = [
            Prey(PreyType.FIELD_MOUSE, pos)
            for pos in [prey_positions.pop() for _ in range(150)]
        ]
        house_mice = [
            Prey(PreyType.HOUSE_MOUSE, pos)
            for pos in [prey_positions.pop() for _ in range(80)]
        ]
        snails = [
            Prey(PreyType.SNAIL, pos)
            for pos in [prey_positions.pop() for _ in range(90)]
        ]
        leafs = [
            Prey(PreyType.LEAF, pos)
            for pos in [prey_positions.pop() for _ in range(300)]
        ]
        rocks = [
            Prey(PreyType.ROCK, pos)
            for pos in [prey_positions.pop() for _ in range(200)]
        ]

        return [
            *field_mice,
            *house_mice,
            *snails,
            *leafs,
            *rocks,
        ]


class Cat:
    def __init__(self, weights: dict):
        self.weights = weights
        
        self.time_left = 2 * 60 * 60  # sec
        self.hunted_preys = []
        self.score = 0
        self.path = []
        self.preys = []
        self.position = HOME

    def __repr__(self) -> str:
        return f'Cat(score={self.score:3f}, pos={self.position})'

    @property
    def position(self):
        return self.x, self.y
    
    @position.setter
    def position(self, value):
        self.x, self.y = value
        self.path.append(value)

    def start_hunting(self):
        # hunt 5 preys, unless there is no more
        while best_prey := self.find_best_prey():            
            self.hunt_prey(best_prey)
            if len(self.hunted_preys) == 5:
                break
        
        # time out or no more preys
        if self.position == HOME:
            return

        self.go_home()
        

    def hunt_prey(self, prey: Prey):
        # go to the prey
        self.time_left -= get_distance(self.position, prey.position) * 0.1
        self.position = prey.position
        
        # hunt down
        self.time_left -= prey.time_to_hunt_down
        self.hunted_preys.append(prey)        
        
        # update score
        self.score += self.weights[prey.type]

        # remove prey 
        self.preys.remove(prey)

    def go_home(self):
        # update position and time
        self.time_left -= get_distance(self.position, HOME) * 0.1
        self.position = HOME

        # go hunting
        self.hunted_preys = []
        self.start_hunting()        

    def find_best_prey(self):
        # objective function is set to hunt down the most precious one
        preys = [
            p for p in self.preys
            if self.can_be_hunted(p) and self.weights[p.type] > 0
        ]   

        if not preys:
            return None  

        # sort based on value, then distance
        preys = sorted(
            preys, 
            key=lambda p: (self.weights[p.type], -get_distance(self.position, p.position)),
            reverse=True
        )

        return preys[0] if preys else None

    def can_be_hunted(self, prey: Prey):
        time_left = self.time_left        

        # go to the prey
        time_left -= get_distance(self.position, prey.position)
        new_pos = prey.position
        
        # hunt down
        time_left -= prey.time_to_hunt_down

        # time to get to home
        time_left -= get_distance(new_pos, HOME)

        return time_left >= 0 


def get_distance(pos1, pos2):
    # calc distance using manhattan formula
    # return abs(pos1[0] - pos2[0]) + abs(pos1[1] - pos2[1])
    return math.dist(pos1, pos2)


def vizualize(preys: list[Prey], paths: list[list[tuple]]):
    try:
        import matplotlib.pyplot as plt
    except ImportError:
        print('In order to visualize paths, you need to download matplotlib with "pip install matplotlib".')

    colors = {
        PreyType.FIELD_MOUSE: 'green',
        PreyType.HOUSE_MOUSE: 'blue',
        PreyType.SNAIL: 'orange',
        PreyType.ROCK: 'grey',
        PreyType.LEAF: 'brown',
    }

    # uncomment this code to show preys
    # for p in preys:
    #     plt.scatter(p.x, p.y, color=colors[p.type])

    for path in paths:
        x_points = [x for x, _ in path]
        y_points = [y for _, y in path]
        plt.plot(x_points, y_points)
    
        
    plt.xlabel('X-axis')
    plt.ylabel('Y-axis')
    plt.title('Positions of Objects and Cats')
    plt.show()


def split_zones(preys: list[Prey], cats: list[Cat]):
    zone1 = []
    zone2 = []
    zone3 = []
    
    f1 = lambda x: -x + AREA[0]
        
    for p in preys:
        if p.x >= HOME[0] and p.y > f1(p.x):
            zone1.append(p)
        elif p.x < HOME[0] and p.y > p.x:
            zone2.append(p)
        else:
            zone3.append(p)

    current_max = float('-inf')

    zones = [zone1, zone2, zone3]    
    
    for z1, z2, z3 in itertools.permutations(zones, 3):
        s1 = len([p for p in z1 if p.type in {PreyType.FIELD_MOUSE,PreyType.HOUSE_MOUSE }]) / (150 + 80)
        s2 = len([p for p in z2 if p.type == PreyType.SNAIL]) / 90
        s3 = len([p for p in z3 if p.type in {PreyType.ROCK,PreyType.LEAF }]) / (300 + 200)

        s = (s1 + s2 + s3) / 3
        
        if s > current_max:
            current_max = s
            zones = [z1, z2, z3]

    for i, cat in enumerate(cats):
        cat.preys = zones[i]
    

def main():
    luna = Cat(
        weights= {
        PreyType.FIELD_MOUSE: 0.4,
        PreyType.HOUSE_MOUSE: 0.4,
        PreyType.SNAIL: 0.1,
        PreyType.ROCK: 0.1,
        PreyType.LEAF: 0,
    })

    adriana = Cat(
        weights= {
        PreyType.FIELD_MOUSE: 0.125,
        PreyType.HOUSE_MOUSE: 0.125,
        PreyType.SNAIL: 0.375,
        PreyType.ROCK: 0,
        PreyType.LEAF: 0.375,
    })

    dante = Cat(
        weights= {
        PreyType.FIELD_MOUSE: 0.2,
        PreyType.HOUSE_MOUSE: 0.2,
        PreyType.SNAIL: 0.05,
        PreyType.ROCK: 0.5,
        PreyType.LEAF: 0.05,
    })

    cats = [luna, adriana, dante]

    preys = Prey.get_preys() 

    split_zones(preys, cats)    

    print(f'Ocena obszaru Luny: {len([p for p in luna.preys if p.type in {PreyType.FIELD_MOUSE,PreyType.HOUSE_MOUSE }])} / {150 + 80}')
    print(f'Ocena obszaru Adriany: {len([p for p in luna.preys if p.type == PreyType.SNAIL])} / {90}')
    print(f'Ocena obszaru Dantego: {len([p for p in luna.preys if p.type in {PreyType.ROCK, PreyType.LEAF}])} / {300 + 200}')


main()

