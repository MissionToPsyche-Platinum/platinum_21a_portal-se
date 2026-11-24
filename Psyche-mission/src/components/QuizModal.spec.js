import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import QuizModal from './QuizModal.vue'

async function chooseOption(wrapper, label) {
  const option = wrapper.findAll('.option-button').find((button) => button.text() === label)
  expect(option).toBeTruthy()
  await option.trigger('click')
  await vi.advanceTimersByTimeAsync(280)
}

describe('QuizModal', () => {
  beforeEach(() => {
    vi.useFakeTimers()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  it('keeps View Results disabled until all four questions are answered', async () => {
    const wrapper = mount(QuizModal)
    const viewResults = wrapper.get('.btn-primary')

    expect(viewResults.attributes('disabled')).toBeDefined()

    await chooseOption(wrapper, 'Laptop/Desktop')
    expect(viewResults.attributes('disabled')).toBeDefined()

    await chooseOption(wrapper, 'Easy')
    await chooseOption(wrapper, 'Exploring space')

    expect(viewResults.attributes('disabled')).toBeDefined()

    await chooseOption(wrapper, 'Psyche asteroid')

    expect(wrapper.get('.btn-primary').attributes('disabled')).toBeUndefined()
  })

  it('emits quiz answers when View Results is clicked', async () => {
    const wrapper = mount(QuizModal)

    await chooseOption(wrapper, 'Laptop/Desktop')
    await chooseOption(wrapper, 'Easy')
    await chooseOption(wrapper, 'Exploring space')
    await chooseOption(wrapper, 'Psyche asteroid')
    await wrapper.get('.btn-primary').trigger('click')

    expect(wrapper.emitted('quiz-complete')).toBeTruthy()
    expect(wrapper.emitted('quiz-complete')[0][0]).toEqual({
      device: 'Laptop/Desktop',
      difficulty: 'Easy',
      fun: 'Exploring space',
      interest: 'Psyche asteroid'
    })
  })

  it('emits close when Close is clicked', async () => {
    const wrapper = mount(QuizModal)

    await wrapper.get('.btn').trigger('click')

    expect(wrapper.emitted('close')).toBeTruthy()
  })
})
