import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import Filter from './Filter.vue'

describe('Filter', () => {
  it('lists class years that match the game catalog', async () => {
    const wrapper = mount(Filter)

    await wrapper.get('.filter-toggle').trigger('click')

    const classValues = wrapper.findAll('select').at(0).findAll('option').map((option) => option.element.value)

    expect(classValues).toContain('Iridium - 2024-2025')
    expect(classValues).toContain('Nickel - 2020-2021')
    expect(classValues.every((value) => !/\(\d+\)/.test(value))).toBe(true)
  })

  it('resetAll clears filters and sort', async () => {
    const wrapper = mount(Filter)
    wrapper.vm.filters.class = 'Iridium - 2024-2025'
    wrapper.vm.sortBy = 'title-asc'

    wrapper.vm.resetAll()
    await wrapper.vm.$nextTick()

    expect(wrapper.vm.filters.class).toBe('')
    expect(wrapper.vm.sortBy).toBe('')
    expect(wrapper.emitted('sort-games').at(-1)[0]).toBe('')
  })
})
