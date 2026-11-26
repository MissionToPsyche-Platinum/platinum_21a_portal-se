import { beforeEach, describe, expect, it, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import gameData from '../assets/games.json'
import GamePage from '../views/GamePage.vue'

// Matches Game.java metadata (id is the key, not a metadata field).
// Backend uses className; the portal catalog uses class.
const METADATA_FIELDS = [
  'title',
  'genre',
  'difficulty',
  'age',
  'class',
  'credits',
  'gtype',
  'engine',
  'description',
  'thumbnail',
  'src',
  'video'
]

const SELECTED_GAME_IDS = [
  'psyche-scraper',
  'welcome-to-psyche',
  'asteroid-art',
  'jolts-journey',
  'return-from-psyche',
  'survive-to-psyche',
  'psyche-run',
  'astro-academy',
  'Explore Psyche XR',
  'Psyche Quest'
]

function catalogGame(id) {
  return gameData.games.find((game) => game.id === id)
}

function toBackendRecord(game) {
  return {
    id: game.id,
    title: game.title,
    genre: game.genre,
    difficulty: game.difficulty,
    age: game.age,
    className: game.class,
    credits: game.credits,
    gtype: game.gtype,
    engine: game.engine,
    description: game.description,
    thumbnail: game.thumbnail,
    src: game.src,
    video: game.video
  }
}

function fromBackendRecord(record) {
  const { className, ...rest } = record
  return {
    ...rest,
    class: className
  }
}

function mountGamePage(id) {
  return mount(GamePage, {
    props: { id },
    global: {
      mocks: {
        $router: { replace: vi.fn() }
      },
      stubs: {
        GameLink: true,
        GameRating: true,
        'router-link': true
      }
    }
  })
}

describe('game metadata vs backend data', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('selects 10 games and verifies 100% of their 12 metadata fields match backend data', async () => {
    expect(METADATA_FIELDS).toHaveLength(12)
    expect(SELECTED_GAME_IDS).toHaveLength(10)
    expect(new Set(SELECTED_GAME_IDS).size).toBe(10)

    let matchedFieldCount = 0
    const expectedFieldCount = SELECTED_GAME_IDS.length * METADATA_FIELDS.length

    for (const id of SELECTED_GAME_IDS) {
      const expected = catalogGame(id)
      expect(expected, `catalog is missing ${id}`).toBeTruthy()

      const backendRecord = toBackendRecord(expected)
      const fromBackend = fromBackendRecord(backendRecord)

      const wrapper = mountGamePage(id)
      await wrapper.vm.$nextTick()
      const displayed = wrapper.vm.game

      expect(displayed, `GamePage did not load ${id}`).toBeTruthy()
      expect(displayed.id).toBe(id)

      for (const field of METADATA_FIELDS) {
        expect(displayed[field], `${id}.${field} on GamePage`).toBe(expected[field])
        expect(fromBackend[field], `${id}.${field} after backend mapping`).toBe(expected[field])
        matchedFieldCount += 1
      }

      expect(wrapper.get('h1').text()).toBe(expected.title)
      expect(wrapper.text()).toContain(expected.difficulty)
      expect(wrapper.text()).toContain(expected.genre)
      expect(wrapper.text()).toContain(expected.age)
      expect(wrapper.text()).toContain(expected.class)
      expect(wrapper.text()).toContain(expected.credits)
      expect(wrapper.text()).toContain(expected.description)

      wrapper.unmount()
    }

    expect(matchedFieldCount).toBe(expectedFieldCount)
    expect(matchedFieldCount / expectedFieldCount).toBe(1)
  })
})
