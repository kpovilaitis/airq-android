package lt.kepo.airq.db

interface Builder<Dto, Model> {
    fun build(dto: Dto) : Model
}