import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import HomePage from './HomePage.vue'

function mockMatchMedia() {
  window.matchMedia = (query) => ({
    matches: false,
    media: query,
    addEventListener() {},
    removeEventListener() {},
    addListener() {},
    removeListener() {},
    dispatchEvent() {
      return false
    }
  })
}

function mountHomePage() {
  return mount(HomePage, {
    global: {
      stubs: {
        FeaturedGame: true,
        GameLink: true,
        QuizModal: true,
        'router-link': true
      }
    }
  })
}

describe('HomePage empty search', () => {
  let wrapper

  beforeEach(() => {
    localStorage.clear()
    mockMatchMedia()
  })

  afterEach(() => {
    if (wrapper) {
      wrapper.unmount()
    }
  })

  it('shows an empty message for a nonsense search and restores games when filters are cleared', async () => {
    wrapper = mountHomePage()

    expect(wrapper.find('.empty-results').exists()).toBe(false)
    expect(wrapper.find('.game-shelves').exists()).toBe(true)

    wrapper.vm.searchRequest = 'zzzzz-not-a-real-game'
    await wrapper.vm.$nextTick()

    expect(wrapper.get('.empty-results-message').text()).toBe('No games match your search')
    expect(wrapper.find('.game-shelves').exists()).toBe(false)

    await wrapper.get('.empty-results button').trigger('click')

    expect(wrapper.vm.searchRequest).toBe('')
    expect(wrapper.find('.empty-results').exists()).toBe(false)
    expect(wrapper.find('.game-shelves').exists()).toBe(true)
  })

  it('fills quiz results after the quiz is completed', async () => {
    wrapper = mountHomePage()

    wrapper.vm.handleQuizResults({
      device: 'Laptop/Desktop',
      difficulty: 'Easy',
      fun: 'Exploring space',
      interest: 'Psyche asteroid'
    })
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.showQuizResults).toBe(true)
    expect(wrapper.vm.quizResults.length).toBeGreaterThan(0)
    expect(wrapper.vm.quizResults.every((game) => game.title)).toBe(true)
  })

  it('keeps only Iridium games when that class is selected', async () => {
    wrapper = mountHomePage()

    wrapper.vm.activeFilters.class = 'Iridium - 2024-2025'
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.filteredGames.length).toBeGreaterThan(0)
    expect(wrapper.vm.filteredGames.every((game) => game.class === 'Iridium - 2024-2025')).toBe(true)
  })

  it('hides AR and VR games for the Web Based platform filter', async () => {
    wrapper = mountHomePage()

    wrapper.vm.activeFilters.genre = 'Web Based'
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.filteredGames.length).toBeGreaterThan(0)
    expect(wrapper.vm.filteredGames.every((game) => game.genre !== 'AR Experience' && game.genre !== 'VR Experience')).toBe(true)
  })

  it('sorts game titles A to Z', async () => {
    wrapper = mountHomePage()

    wrapper.vm.sortBy = 'title-asc'
    await wrapper.vm.$nextTick()

    const titles = wrapper.vm.sortGames.map((game) => game.title)
    expect(titles).toEqual([...titles].sort((a, b) => a.localeCompare(b)))
  })

  it('shows an empty favorites message when favorites-only is on and none are saved', async () => {
    wrapper = mountHomePage()

    wrapper.vm.favoritesOnly = true
    wrapper.vm.favoriteIds = []
    await wrapper.vm.$nextTick()

    expect(wrapper.get('.empty-favorites-message').text()).toContain('No favorite games saved yet')
  })

  it('keeps only Easy games when that difficulty is selected', async () => {
    wrapper = mountHomePage()

    wrapper.vm.activeFilters.difficulty = 'Easy'
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.filteredGames.length).toBeGreaterThan(0)
    expect(wrapper.vm.filteredGames.every((game) => game.difficulty === 'Easy')).toBe(true)
  })

  it('keeps only High School games when that age is selected', async () => {
    wrapper = mountHomePage()

    wrapper.vm.activeFilters.age = 'High School'
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.filteredGames.length).toBeGreaterThan(0)
    expect(wrapper.vm.filteredGames.every((game) => game.age === 'High School')).toBe(true)
  })

  it('filters games by title search', async () => {
    wrapper = mountHomePage()

    wrapper.vm.searchRequest = 'jolt'
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.filteredGames.some((game) => game.id === 'jolts-journey')).toBe(true)
    expect(wrapper.vm.filteredGames.every((game) => game.title.toLowerCase().includes('jolt') || (game.description || '').toLowerCase().includes('jolt'))).toBe(true)
  })

  it('sorts game titles Z to A', async () => {
    wrapper = mountHomePage()

    wrapper.vm.sortBy = 'title-desc'
    await wrapper.vm.$nextTick()

    const titles = wrapper.vm.sortGames.map((game) => game.title)
    expect(titles).toEqual([...titles].sort((a, b) => b.localeCompare(a)))
  })

  it('toggles the favorites-only list from the browse bar', async () => {
    wrapper = mountHomePage()

    const favoritesButton = wrapper.findAll('button').find((button) => button.text().includes('Show Favorite Games'))
    expect(favoritesButton).toBeTruthy()

    await favoritesButton.trigger('click')

    expect(wrapper.vm.favoritesOnly).toBe(true)
    expect(wrapper.text()).toContain('Show All Games')
  })

  it('saves light mode when the theme is toggled', () => {
    wrapper = mountHomePage()

    expect(wrapper.vm.isDark).toBe(true)
    wrapper.vm.toggleMode()

    expect(wrapper.vm.isDark).toBe(false)
    expect(localStorage.getItem('savedMode')).toBe('Light')
  })
})


