import { describe, expect, it } from 'vitest'
import gameData from '../assets/games.json'
import { getSuggestions } from './gameService'

describe('getSuggestions', () => {
  it('returns up to 3 other games for a known id', () => {
    const suggestions = getSuggestions('psyche-scraper')

    expect(Array.isArray(suggestions)).toBe(true)
    expect(suggestions.length).toBeLessThanOrEqual(3)
    expect(suggestions.every((game) => game.id !== 'psyche-scraper')).toBe(true)
  })

  it('returns unique games', () => {
    const suggestions = getSuggestions('psyche-scraper')
    const ids = suggestions.map((game) => game.id)

    expect(new Set(ids).size).toBe(ids.length)
  })

  it('uses the local gif for Jolt\'s Journey hover media', () => {
    const jolts = gameData.games.find((game) => game.id === 'jolts-journey')

    expect(jolts).toBeTruthy()
    expect(jolts.video).toBe('/gif/jolts_journey.gif')
    expect(jolts.class).toBe('Nickel - 2020-2021')
  })
})

